import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A single-file, enterprise-style payment settlement example for an Indian
 * marketplace. It demonstrates collection allocation between the platform's
 * main operating account, a vendor, a delivery partner, the payment gateway,
 * and the government (tax withheld at source).
 *
 * <p>This is an educational accounting model, not tax advice. Actual GST/TDS/
 * TCS applicability, rates, place-of-supply rules, invoice ownership and
 * rounding must be confirmed by a qualified tax professional.</p>
 *
 * <p>Important production ideas represented here:</p>
 * <ul>
 *   <li>Money uses {@link BigDecimal}, never {@code double}.</li>
 *   <li>GST is split into CGST + SGST for intra-state supply, or IGST for
 *       inter-state supply.</li>
 *   <li>Commission, gateway charges and withholding are explicit ledger
 *       transfers rather than hidden arithmetic.</li>
 *   <li>Every settlement is idempotent and has a controlled state transition.</li>
 *   <li>Double-entry journal entries and a final conservation check make the
 *       calculation auditable and reconcilable.</li>
 * </ul>
 */
public final class PaymentSettlement {

    private static final int MONEY_SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private PaymentSettlement() {
    }

    public static void main(String[] args) {
        SettlementService service = new SettlementService();

        SettlementRequest request = new SettlementRequest(
                "SETTLE-ORDER-2026-0001", "ORDER-2026-0001", "INR",
                "KA", "KA",
                List.of(
                        new Charge("Food supplied by vendor", Account.VENDOR,
                                money("1000.00"), rate("5.00")),
                        new Charge("Delivery service", Account.DELIVERY_PARTNER,
                                money("100.00"), rate("18.00")),
                        new Charge("Platform convenience fee", Account.PLATFORM_OPERATIONS,
                                money("50.00"), rate("18.00"))
                ),
                rate("20.00"),  // platform commission on vendor taxable value
                rate("18.00"),  // GST on the platform's commission service
                rate("1.00"),   // illustrative vendor withholding
                rate("1.00"),   // illustrative delivery-partner withholding
                money("25.00"), // gateway taxable fee
                rate("18.00")   // GST on gateway fee
        );

        Settlement settlement = service.create(request);
        service.approve(settlement.id, "finance.approver@example.com");
        service.markProcessing(settlement.id);

        // A real implementation calls bank/payout APIs and stores each response.
        for (PayoutInstruction payout : settlement.payouts) {
            System.out.printf("PAY %-22s %8s %s  reference=%s%n",
                    payout.beneficiary, payout.amount, payout.currency,
                    payout.externalReference);
        }
        service.markSettled(settlement.id);
        printReport(settlement);
    }

    private static void printReport(Settlement settlement) {
        System.out.println("\n===== SETTLEMENT SUMMARY =====");
        System.out.println("Settlement : " + settlement.id);
        System.out.println("Order      : " + settlement.orderId);
        System.out.println("Status     : " + settlement.status);
        System.out.println("Collected  : " + settlement.customerCollection + " " + settlement.currency);

        System.out.println("\nGST BREAKDOWN");
        for (TaxLine tax : settlement.taxLines) {
            System.out.printf("  %-34s taxable=%8s CGST=%6s SGST=%6s IGST=%6s%n",
                    tax.description, tax.taxableValue, tax.cgst, tax.sgst, tax.igst);
        }

        System.out.println("\nFINAL ACCOUNT BALANCES / PAYOUTS");
        settlement.finalBalances.forEach((account, amount) ->
                System.out.printf("  %-24s %8s%n", account, amount));

        System.out.println("\nJOURNAL (each row balances: debit = credit)");
        for (JournalEntry entry : settlement.journal) {
            System.out.printf("  %-42s DR %-22s CR %-22s %8s%n",
                    entry.narration, entry.debit, entry.credit, entry.amount);
        }

        System.out.println("\nReconciliation: payouts " + settlement.totalDisbursed
                + " = customer collection " + settlement.customerCollection
                + " -> " + (settlement.reconciled ? "PASS" : "FAIL"));
    }

    /** Coordinates validation, calculation, persistence and state transitions. */
    public static final class SettlementService {
        // These repositories are in-memory only so the example remains one file.
        private final Map<String, Settlement> settlements = new HashMap<>();
        private final Map<String, String> idempotencyIndex = new HashMap<>();

        public synchronized Settlement create(SettlementRequest request) {
            Objects.requireNonNull(request, "request");
            request.validate();

            String existingId = idempotencyIndex.get(request.idempotencyKey);
            if (existingId != null) {
                return settlements.get(existingId); // safe retry: no duplicate payout
            }

            Settlement calculated = new SettlementCalculator().calculate(request);
            settlements.put(calculated.id, calculated);
            idempotencyIndex.put(request.idempotencyKey, calculated.id);
            calculated.audit("SYSTEM", "CREATED", "Settlement calculated and reconciled");
            return calculated;
        }

        public synchronized void approve(String settlementId, String approver) {
            Settlement settlement = get(settlementId);
            settlement.transition(Status.CREATED, Status.APPROVED);
            settlement.audit(requireText(approver, "approver"), "APPROVED",
                    "Four-eyes approval completed");
        }

        public synchronized void markProcessing(String settlementId) {
            Settlement settlement = get(settlementId);
            settlement.transition(Status.APPROVED, Status.PROCESSING);
            settlement.audit("PAYOUT_WORKER", "PROCESSING", "Payout batch submitted");
        }

        public synchronized void markSettled(String settlementId) {
            Settlement settlement = get(settlementId);
            settlement.transition(Status.PROCESSING, Status.SETTLED);
            settlement.audit("RECONCILIATION_WORKER", "SETTLED",
                    "All bank references reconciled");
        }

        public Settlement get(String id) {
            Settlement result = settlements.get(id);
            if (result == null) {
                throw new IllegalArgumentException("Unknown settlement: " + id);
            }
            return result;
        }
    }

    /** Pure deterministic calculator: no database, clock, network, or payout calls. */
    public static final class SettlementCalculator {

        public Settlement calculate(SettlementRequest request) {
            EnumMap<Account, BigDecimal> balances = zeroBalances();
            List<TaxLine> taxLines = new ArrayList<>();
            List<JournalEntry> journal = new ArrayList<>();
            BigDecimal collection = money("0");

            // Customer charge: base + applicable GST belongs initially to the
            // supplier named on that charge. GST is reported separately but is
            // paid to that supplier, who owns the statutory remittance duty.
            for (Charge charge : request.charges) {
                TaxLine tax = calculateGst(charge.description, charge.taxableValue,
                        charge.gstRate, request.supplierState, request.placeOfSupplyState);
                taxLines.add(tax);
                BigDecimal gross = add(charge.taxableValue, tax.totalTax());
                collection = add(collection, gross);
                transfer(balances, journal, Account.CUSTOMER_CLEARING, charge.owner,
                        gross, "Allocate customer charge: " + charge.description);
            }

            Charge vendorCharge = request.charges.stream()
                    .filter(c -> c.owner == Account.VENDOR)
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("A vendor charge is required"));

            // The platform invoices the vendor for commission plus GST.
            BigDecimal commissionBase = percentage(vendorCharge.taxableValue,
                    request.platformCommissionRate);
            TaxLine commissionTax = calculateGst("Platform commission to vendor",
                    commissionBase, request.commissionGstRate,
                    request.supplierState, request.placeOfSupplyState);
            taxLines.add(commissionTax);
            transfer(balances, journal, Account.VENDOR, Account.PLATFORM_OPERATIONS,
                    add(commissionBase, commissionTax.totalTax()),
                    "Platform commission including GST");

            // Withholding is not revenue. It is moved to a government payable
            // account and remitted using the beneficiary's tax identity.
            BigDecimal vendorWithholding = percentage(vendorCharge.taxableValue,
                    request.vendorWithholdingRate);
            transfer(balances, journal, Account.VENDOR, Account.GOVERNMENT_WITHHOLDING,
                    vendorWithholding, "Vendor tax withheld at source");

            BigDecimal deliveryBase = request.charges.stream()
                    .filter(c -> c.owner == Account.DELIVERY_PARTNER)
                    .map(c -> c.taxableValue).reduce(money("0"), PaymentSettlement::add);
            BigDecimal deliveryWithholding = percentage(deliveryBase,
                    request.deliveryWithholdingRate);
            transfer(balances, journal, Account.DELIVERY_PARTNER,
                    Account.GOVERNMENT_WITHHOLDING, deliveryWithholding,
                    "Delivery partner tax withheld at source");

            // The gateway deducts its invoice from the platform's earnings.
            TaxLine gatewayTax = calculateGst("Payment gateway processing fee",
                    request.gatewayFee, request.gatewayGstRate,
                    request.supplierState, request.placeOfSupplyState);
            taxLines.add(gatewayTax);
            transfer(balances, journal, Account.PLATFORM_OPERATIONS,
                    Account.PAYMENT_GATEWAY,
                    add(request.gatewayFee, gatewayTax.totalTax()),
                    "Gateway fee including GST");

            requireNonNegativeBeneficiaries(balances);
            balances.put(Account.CUSTOMER_CLEARING, collection); // memorandum balance

            List<PayoutInstruction> payouts = createPayouts(balances, request.currency,
                    request.orderId);
            BigDecimal disbursed = payouts.stream().map(p -> p.amount)
                    .reduce(money("0"), PaymentSettlement::add);
            if (disbursed.compareTo(collection) != 0) {
                throw new ReconciliationException("Out of balance: collected=" + collection
                        + ", disbursed=" + disbursed);
            }

            return new Settlement("STL-" + UUID.randomUUID(), request.orderId,
                    request.currency, collection, balances, taxLines, journal,
                    payouts, disbursed, true);
        }

        private static void transfer(Map<Account, BigDecimal> balances,
                                     List<JournalEntry> journal,
                                     Account from, Account to, BigDecimal amount,
                                     String narration) {
            requireNonNegative(amount, "transfer amount");
            if (amount.signum() == 0) {
                return;
            }
            // CUSTOMER_CLEARING is the funding source and is tracked separately;
            // later transfers move already allocated beneficiary balances.
            if (from != Account.CUSTOMER_CLEARING) {
                BigDecimal available = balances.get(from);
                if (available.compareTo(amount) < 0) {
                    throw new ValidationException(from + " has insufficient settlement balance");
                }
                balances.put(from, subtract(available, amount));
            }
            balances.put(to, add(balances.get(to), amount));
            journal.add(new JournalEntry(to, from, amount, narration));
        }

        private static List<PayoutInstruction> createPayouts(
                Map<Account, BigDecimal> balances, String currency, String orderId) {
            List<PayoutInstruction> result = new ArrayList<>();
            for (Account account : Account.values()) {
                if (account == Account.CUSTOMER_CLEARING) {
                    continue;
                }
                BigDecimal amount = balances.get(account);
                if (amount.signum() > 0) {
                    result.add(new PayoutInstruction(account, amount, currency,
                            orderId + "-" + account.name()));
                }
            }
            return Collections.unmodifiableList(result);
        }

        private static void requireNonNegativeBeneficiaries(Map<Account, BigDecimal> balances) {
            for (Map.Entry<Account, BigDecimal> entry : balances.entrySet()) {
                if (entry.getKey() != Account.CUSTOMER_CLEARING
                        && entry.getValue().signum() < 0) {
                    throw new ValidationException("Negative payout for " + entry.getKey());
                }
            }
        }
    }

    public enum Account {
        CUSTOMER_CLEARING,
        PLATFORM_OPERATIONS,
        VENDOR,
        DELIVERY_PARTNER,
        PAYMENT_GATEWAY,
        GOVERNMENT_WITHHOLDING
    }

    public enum Status { CREATED, APPROVED, PROCESSING, SETTLED, FAILED }

    public static final class SettlementRequest {
        public final String idempotencyKey;
        public final String orderId;
        public final String currency;
        public final String supplierState;
        public final String placeOfSupplyState;
        public final List<Charge> charges;
        public final BigDecimal platformCommissionRate;
        public final BigDecimal commissionGstRate;
        public final BigDecimal vendorWithholdingRate;
        public final BigDecimal deliveryWithholdingRate;
        public final BigDecimal gatewayFee;
        public final BigDecimal gatewayGstRate;

        public SettlementRequest(String idempotencyKey, String orderId, String currency,
                                 String supplierState, String placeOfSupplyState,
                                 List<Charge> charges, BigDecimal platformCommissionRate,
                                 BigDecimal commissionGstRate, BigDecimal vendorWithholdingRate,
                                 BigDecimal deliveryWithholdingRate, BigDecimal gatewayFee,
                                 BigDecimal gatewayGstRate) {
            this.idempotencyKey = idempotencyKey;
            this.orderId = orderId;
            this.currency = currency;
            this.supplierState = supplierState;
            this.placeOfSupplyState = placeOfSupplyState;
            this.charges = charges == null ? List.of() : List.copyOf(charges);
            this.platformCommissionRate = platformCommissionRate;
            this.commissionGstRate = commissionGstRate;
            this.vendorWithholdingRate = vendorWithholdingRate;
            this.deliveryWithholdingRate = deliveryWithholdingRate;
            this.gatewayFee = gatewayFee;
            this.gatewayGstRate = gatewayGstRate;
        }

        void validate() {
            requireText(idempotencyKey, "idempotencyKey");
            requireText(orderId, "orderId");
            if (!"INR".equals(currency)) {
                throw new ValidationException("This example supports INR only");
            }
            requireText(supplierState, "supplierState");
            requireText(placeOfSupplyState, "placeOfSupplyState");
            if (charges.isEmpty()) {
                throw new ValidationException("At least one charge is required");
            }
            for (Charge charge : charges) {
                Objects.requireNonNull(charge, "charge").validate();
            }
            validateRate(platformCommissionRate, "platformCommissionRate");
            validateRate(commissionGstRate, "commissionGstRate");
            validateRate(vendorWithholdingRate, "vendorWithholdingRate");
            validateRate(deliveryWithholdingRate, "deliveryWithholdingRate");
            validateRate(gatewayGstRate, "gatewayGstRate");
            requireNonNegative(gatewayFee, "gatewayFee");
        }
    }

    public static final class Charge {
        public final String description;
        public final Account owner;
        public final BigDecimal taxableValue;
        public final BigDecimal gstRate;

        public Charge(String description, Account owner, BigDecimal taxableValue,
                      BigDecimal gstRate) {
            this.description = description;
            this.owner = owner;
            this.taxableValue = normalize(taxableValue);
            this.gstRate = normalize(gstRate);
        }

        void validate() {
            requireText(description, "charge.description");
            if (owner == null || owner == Account.CUSTOMER_CLEARING
                    || owner == Account.GOVERNMENT_WITHHOLDING) {
                throw new ValidationException("Invalid charge owner: " + owner);
            }
            requireNonNegative(taxableValue, "charge.taxableValue");
            validateRate(gstRate, "charge.gstRate");
        }
    }

    public static final class TaxLine {
        public final String description;
        public final BigDecimal taxableValue;
        public final BigDecimal cgst;
        public final BigDecimal sgst;
        public final BigDecimal igst;

        TaxLine(String description, BigDecimal taxableValue, BigDecimal cgst,
                BigDecimal sgst, BigDecimal igst) {
            this.description = description;
            this.taxableValue = taxableValue;
            this.cgst = cgst;
            this.sgst = sgst;
            this.igst = igst;
        }

        public BigDecimal totalTax() {
            return add(add(cgst, sgst), igst);
        }
    }

    public static final class JournalEntry {
        public final Account debit;
        public final Account credit;
        public final BigDecimal amount;
        public final String narration;

        JournalEntry(Account debit, Account credit, BigDecimal amount, String narration) {
            this.debit = debit;
            this.credit = credit;
            this.amount = amount;
            this.narration = narration;
        }
    }

    public static final class PayoutInstruction {
        public final Account beneficiary;
        public final BigDecimal amount;
        public final String currency;
        public final String externalReference;

        PayoutInstruction(Account beneficiary, BigDecimal amount, String currency,
                          String externalReference) {
            this.beneficiary = beneficiary;
            this.amount = amount;
            this.currency = currency;
            this.externalReference = externalReference;
        }
    }

    public static final class AuditEvent {
        public final Instant occurredAt;
        public final String actor;
        public final String action;
        public final String detail;

        AuditEvent(Instant occurredAt, String actor, String action, String detail) {
            this.occurredAt = occurredAt;
            this.actor = actor;
            this.action = action;
            this.detail = detail;
        }
    }

    public static final class Settlement {
        public final String id;
        public final String orderId;
        public final String currency;
        public final BigDecimal customerCollection;
        public final Map<Account, BigDecimal> finalBalances;
        public final List<TaxLine> taxLines;
        public final List<JournalEntry> journal;
        public final List<PayoutInstruction> payouts;
        public final BigDecimal totalDisbursed;
        public final boolean reconciled;
        public final List<AuditEvent> auditTrail = new ArrayList<>();
        public Status status = Status.CREATED;

        Settlement(String id, String orderId, String currency,
                   BigDecimal customerCollection, Map<Account, BigDecimal> balances,
                   List<TaxLine> taxLines, List<JournalEntry> journal,
                   List<PayoutInstruction> payouts, BigDecimal totalDisbursed,
                   boolean reconciled) {
            this.id = id;
            this.orderId = orderId;
            this.currency = currency;
            this.customerCollection = customerCollection;
            this.finalBalances = Collections.unmodifiableMap(new LinkedHashMap<>(balances));
            this.taxLines = Collections.unmodifiableList(new ArrayList<>(taxLines));
            this.journal = Collections.unmodifiableList(new ArrayList<>(journal));
            this.payouts = payouts;
            this.totalDisbursed = totalDisbursed;
            this.reconciled = reconciled;
        }

        void transition(Status expected, Status target) {
            if (status != expected) {
                throw new IllegalStateException("Expected " + expected + " but was " + status);
            }
            status = target;
        }

        void audit(String actor, String action, String detail) {
            auditTrail.add(new AuditEvent(Instant.now(), actor, action, detail));
        }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) { super(message); }
    }

    public static final class ReconciliationException extends RuntimeException {
        public ReconciliationException(String message) { super(message); }
    }

    private static TaxLine calculateGst(String description, BigDecimal taxable,
                                        BigDecimal gstRate, String supplierState,
                                        String placeOfSupplyState) {
        BigDecimal totalTax = percentage(taxable, gstRate);
        if (supplierState.equalsIgnoreCase(placeOfSupplyState)) {
            // Calculate one half and derive the other so paise always reconcile.
            BigDecimal cgst = totalTax.divide(new BigDecimal("2"), MONEY_SCALE, ROUNDING);
            BigDecimal sgst = subtract(totalTax, cgst);
            return new TaxLine(description, taxable, cgst, sgst, money("0"));
        }
        return new TaxLine(description, taxable, money("0"), money("0"), totalTax);
    }

    private static EnumMap<Account, BigDecimal> zeroBalances() {
        EnumMap<Account, BigDecimal> result = new EnumMap<>(Account.class);
        for (Account account : Account.values()) {
            result.put(account, money("0"));
        }
        return result;
    }

    private static BigDecimal percentage(BigDecimal value, BigDecimal rate) {
        return value.multiply(rate).divide(HUNDRED, MONEY_SCALE, ROUNDING);
    }

    private static BigDecimal add(BigDecimal left, BigDecimal right) {
        return left.add(right).setScale(MONEY_SCALE, ROUNDING);
    }

    private static BigDecimal subtract(BigDecimal left, BigDecimal right) {
        return left.subtract(right).setScale(MONEY_SCALE, ROUNDING);
    }

    private static BigDecimal money(String value) {
        return new BigDecimal(value).setScale(MONEY_SCALE, ROUNDING);
    }

    private static BigDecimal rate(String value) {
        return new BigDecimal(value).setScale(MONEY_SCALE, ROUNDING);
    }

    private static BigDecimal normalize(BigDecimal value) {
        if (value == null) {
            throw new ValidationException("Numeric value cannot be null");
        }
        return value.setScale(MONEY_SCALE, ROUNDING);
    }

    private static void validateRate(BigDecimal value, String name) {
        requireNonNegative(value, name);
        if (value.compareTo(HUNDRED) > 0) {
            throw new ValidationException(name + " cannot exceed 100%");
        }
    }

    private static void requireNonNegative(BigDecimal value, String name) {
        if (value == null || value.signum() < 0) {
            throw new ValidationException(name + " must be non-negative");
        }
    }

    private static String requireText(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(name + " is required");
        }
        return value;
    }
}
