# Java Security Best Practices - Complete Guide

## Table of Contents
1. [Authentication & Authorization](#authentication--authorization)
2. [JWT Token Management](#jwt-token-management)
3. [Input Validation & Sanitization](#input-validation--sanitization)
4. [SQL Injection Prevention](#sql-injection-prevention)
5. [XSS & CSRF Protection](#xss--csrf-protection)
6. [Secrets Management](#secrets-management)
7. [Cryptography Best Practices](#cryptography-best-practices)
8. [Security Headers](#security-headers)
9. [Audit Logging](#audit-logging)
10. [Rate Limiting & Brute Force Protection](#rate-limiting--brute-force-protection)

---

## 1. Authentication & Authorization

### What it does
Authentication verifies who you are; authorization determines what you can do.

### Why it matters
- Prevents unauthorized access to resources
- Ensures users can only perform allowed operations
- Provides audit trail for security events

### Intuition
Authentication is showing your ID at the door; authorization is the bouncer checking your VIP wristband.

### Spring Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler(new HttpStatusAccessDeniedHandler(HttpStatus.FORBIDDEN))
            )
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}

// Method-level security
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<OrderDTO> getMyOrders(@AuthenticationPrincipal UserDetails user) {
        return orderService.getOrdersByUser(user.getUsername());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#id, authentication)")
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
    }
}

// Custom security expression
@Component("orderSecurity")
public class OrderSecurityExpression {
    private final OrderRepository orderRepository;

    public boolean isOwner(String orderId, Authentication authentication) {
        return orderRepository.findById(orderId)
            .map(order -> order.getUserId().equals(authentication.getName()))
            .orElse(false);
    }
}
```

---

## 2. JWT Token Management

### What it does
JSON Web Tokens provide stateless authentication by encoding user claims in a signed token verifiable without database lookup.

### Why it matters
- Stateless — no session storage needed
- Self-contained — carries user info and permissions
- Scalable — works across multiple server instances

### Intuition
Like a signed passport — contains identity info, signed by a trusted authority. Anyone can verify it without calling the issuing office.

### JWT Implementation

```java
@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;

    public JwtTokenProvider(
            @Value("${app.security.jwt-secret}") String secret,
            @Value("${app.security.access-token-expiry:3600000}") long accessExpiry,
            @Value("${app.security.refresh-token-expiry:604800000}") long refreshExpiry) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiry = accessExpiry;
        this.refreshTokenExpiry = refreshExpiry;
    }

    public String generateAccessToken(String userId, List<String> roles) {
        return Jwts.builder()
            .subject(userId)
            .claim("roles", roles)
            .claim("type", "access")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    public Claims validateAndExtractClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException("Token has expired");
        } catch (JwtException ex) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}

// JWT Filter
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Claims claims = jwtTokenProvider.validateAndExtractClaims(token);
                String userId = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                log.debug("JWT validation failed: {}", ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (StringUtils.hasText(header) && header.startsWith("Bearer "))
            ? header.substring(7) : null;
    }
}

// Refresh token rotation
@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenPair login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String accessToken = jwtTokenProvider.generateAccessToken(userDetails.getUsername(), roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());

        refreshTokenRepository.save(RefreshToken.builder()
            .userId(userDetails.getUsername())
            .tokenHash(DigestUtils.sha256Hex(refreshToken))
            .expiresAt(LocalDateTime.now().plusDays(7))
            .build());

        return new TokenPair(accessToken, refreshToken);
    }
}
```

---

## 3. Input Validation & Sanitization

### What it does
Validates and sanitizes all incoming data to prevent injection attacks and data corruption.

### Why it matters
- Prevents SQL injection, XSS, command injection
- Ensures data integrity
- First line of defense against malicious input

### Bean Validation

```java
public record CreateUserRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Name contains invalid characters")
    String name,

    @NotBlank @Email(message = "Invalid email format")
    @Size(max = 255)
    String email,

    @NotBlank
    @Size(min = 8, max = 128)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "Password must contain uppercase, lowercase, digit, and special character"
    )
    String password
) {}

// Global exception handler
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ErrorResponse("Validation failed", errors);
    }
}
```

---

## 4. SQL Injection Prevention

### What it does
Prevents attackers from injecting malicious SQL by using parameterized queries and ORM frameworks.

### Why it matters
- SQL injection is the #1 web vulnerability (OWASP Top 10)
- Can expose entire database, delete data, bypass authentication

### Intuition
Input is treated as data, never as executable code — like a form that only accepts typed characters, not commands.

### Safe Query Patterns

```java
// WRONG - vulnerable to SQL injection
String query = "SELECT u FROM User u WHERE u.name = '" + name + "'";

// CORRECT - parameterized JPQL
em.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
    .setParameter("name", name)
    .getResultList();

// Spring Data JPA - safe by default
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByName(String name);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status = :status")
    Optional<User> findByEmailAndStatus(@Param("email") String email,
                                         @Param("status") UserStatus status);
}

// JDBC Template - parameterized
jdbcTemplate.query("SELECT * FROM users WHERE city = ?",
    new Object[]{city}, new UserRowMapper());
```

---

## 5. XSS & CSRF Protection

### What it does
XSS prevention stops malicious script injection. CSRF protection prevents unauthorized commands from trusted users.

### Why it matters
- XSS can steal session cookies, redirect users
- CSRF can perform actions on behalf of authenticated users
- Both are in OWASP Top 10

### Security Headers for XSS

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .headers(headers -> headers
            .contentSecurityPolicy(csp -> csp
                .policyDirectives(
                    "default-src 'self'; script-src 'self'; " +
                    "style-src 'self'; img-src 'self' data:; frame-ancestors 'none'"
                ))
            .frameOptions(frame -> frame.deny())
            .xssProtection(xss -> xss
                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
            .contentTypeOptions(Customizer.withDefaults())
        )
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .build();
}
```

---

## 6. Secrets Management

### What it does
Manages sensitive configuration (passwords, API keys) securely, keeping them out of source code.

### Why it matters
- Secrets in code = security breach waiting to happen
- Enables rotation without redeployment
- Audit trail for secret access

### Intuition
Like a bank vault — only authorized people can access it, every access is logged.

### Environment Variables & Vault

```java
// WRONG - never hardcode secrets
private static final String DB_PASSWORD = "mypassword123";

// CORRECT - environment variables
@Value("${DB_PASSWORD}")
private String dbPassword;

// Secure token generation
@Service
public class TokenGeneratorService {
    public String generateSecureToken(int byteLength) {
        byte[] bytes = new byte[byteLength];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
```

```yaml
# bootstrap.yml - Spring Cloud Vault
spring:
  cloud:
    vault:
      host: vault.example.com
      port: 8200
      scheme: https
      authentication: KUBERNETES
      kubernetes:
        role: user-service
```

---

## 7. Cryptography Best Practices

### What it does
Applies correct cryptographic algorithms for hashing passwords, encrypting data, and generating secure tokens.

### Why it matters
- Weak cryptography is as bad as no cryptography
- Password hashing prevents credential theft
- Encryption protects sensitive data at rest and in transit

### Intuition
Like choosing the right lock — a padlock (MD5) vs a bank vault (bcrypt). The effort to break it must exceed the value.

### Password Hashing & Encryption

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);  // Cost factor 12
}

// AES-GCM encryption for sensitive data
@Service
public class EncryptionService {
    private final SecretKey aesKey;

    public EncryptionService(@Value("${app.encryption.key}") String base64Key) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        this.aesKey = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception ex) {
            throw new EncryptionException("Encryption failed", ex);
        }
    }

    public String decrypt(String encryptedBase64) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedBase64);
            byte[] iv = Arrays.copyOfRange(combined, 0, 12);
            byte[] ciphertext = Arrays.copyOfRange(combined, 12, combined.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));
            return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new EncryptionException("Decryption failed", ex);
        }
    }
}
```

---

## 8. Security Headers

### What it does
HTTP security headers instruct browsers to enforce security policies, protecting against common attacks.

### Why it matters
- Prevents clickjacking (X-Frame-Options)
- Enforces HTTPS (HSTS)
- Prevents MIME sniffing (X-Content-Type-Options)

### Configuration

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .headers(headers -> headers
            .frameOptions(frame -> frame.deny())
            .httpStrictTransportSecurity(hsts -> hsts
                .includeSubDomains(true)
                .maxAgeInSeconds(31536000)
                .preload(true))
            .contentTypeOptions(Customizer.withDefaults())
            .referrerPolicy(referrer ->
                referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            .permissionsPolicy(permissions ->
                permissions.policy("camera=(), microphone=(), geolocation=(self)"))
        )
        .build();
}
```

---

## 9. Audit Logging

### What it does
Records security-relevant events (logins, data access, permission changes) for compliance and forensics.

### Why it matters
- Compliance requirements (GDPR, PCI-DSS, HIPAA)
- Forensic investigation after incidents
- Detect suspicious patterns (brute force, data exfiltration)

### Intuition
Like a bank's security camera — records everything so you can review what happened and who did it.

### Audit Implementation

```java
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String userId;
    private String action;
    private String resourceType;
    private String resourceId;
    private String ipAddress;
    private String userAgent;
    @Enumerated(EnumType.STRING)
    private AuditResult result;
    private String details;
    @CreationTimestamp
    private LocalDateTime timestamp;
}

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public void log(String userId, String action, String resourceType,
                     String resourceId, AuditResult result, String details) {
        AuditLog log = AuditLog.builder()
            .userId(userId)
            .action(action)
            .resourceType(resourceType)
            .resourceId(resourceId)
            .result(result)
            .details(details)
            .build();
        auditLogRepository.save(log);
    }
}

// Audit aspect for automatic logging
@Aspect
@Component
public class AuditAspect {
    private final AuditService auditService;
    private final HttpServletRequest request;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {
        String userId = getCurrentUserId();
        try {
            Object result = pjp.proceed();
            auditService.log(userId, auditable.action(), auditable.resource(),
                extractResourceId(pjp), AuditResult.SUCCESS, null);
            return result;
        } catch (Exception ex) {
            auditService.log(userId, auditable.action(), auditable.resource(),
                extractResourceId(pjp), AuditResult.FAILURE, ex.getMessage());
            throw ex;
        }
    }
}

// Usage
@DeleteMapping("/{id}")
@Auditable(action = "DELETE", resource = "ORDER")
public void deleteOrder(@PathVariable String id) {
    orderService.deleteOrder(id);
}
```

---

## 10. Rate Limiting & Brute Force Protection

### What it does
Limits the number of requests from a client within a time window, preventing brute force attacks and API abuse.

### Why it matters
- Prevents brute force password attacks
- Protects against DDoS
- Ensures fair API usage
- Reduces server load from abusive clients

### Intuition
Like a bank's ATM — locks you out after 3 wrong PIN attempts to prevent guessing.

### Rate Limiting with Bucket4j

```java
@Service
public class RateLimitingService {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket());
        return bucket.tryConsume(1);
    }
}

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private final RateLimitingService rateLimitingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain chain) throws ServletException, IOException {
        String clientKey = getClientKey(request);
        if (!rateLimitingService.tryConsume(clientKey)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader("Retry-After", "60");
            response.getWriter().write("{\"error\": \"Rate limit exceeded\"}");
            return;
        }
        chain.doFilter(request, response);
    }

    private String getClientKey(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return userId != null ? "user:" + userId : "ip:" + request.getRemoteAddr();
    }
}

// Brute force protection for login
@Service
public class LoginAttemptService {
    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockouts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCKOUT_DURATION = Duration.ofMinutes(15);

    public void recordFailedAttempt(String email) {
        int count = attempts.merge(email, 1, Integer::sum);
        if (count >= MAX_ATTEMPTS) {
            lockouts.put(email, LocalDateTime.now().plus(LOCKOUT_DURATION));
            log.warn("Account locked due to too many failed attempts: {}", email);
        }
    }

    public void recordSuccessfulLogin(String email) {
        attempts.remove(email);
        lockouts.remove(email);
    }

    public boolean isLocked(String email) {
        LocalDateTime lockoutTime = lockouts.get(email);
        if (lockoutTime == null) return false;
        if (LocalDateTime.now().isAfter(lockoutTime)) {
            lockouts.remove(email);
            attempts.remove(email);
            return false;
        }
        return true;
    }
}
```

---

## Edge Cases & Best Practices

### Edge Cases
- **JWT secret rotation**: Use key IDs (kid) in JWT header to support multiple active keys during rotation
- **Token replay attacks**: Use short-lived access tokens (15 min) with refresh token rotation
- **Timing attacks**: Use `MessageDigest.isEqual()` for constant-time comparison of secrets
- **Mass assignment**: Use DTOs, never bind request body directly to entity
- **Insecure deserialization**: Never deserialize untrusted data with Java native serialization

### Security Checklist

```java
// Constant-time comparison to prevent timing attacks
public boolean secureEquals(String a, String b) {
    return MessageDigest.isEqual(
        a.getBytes(StandardCharsets.UTF_8),
        b.getBytes(StandardCharsets.UTF_8)
    );
}

// Prevent mass assignment
@PostMapping
public UserDTO createUser(@RequestBody CreateUserRequest request) {
    // WRONG: User user = objectMapper.convertValue(request, User.class);
    // CORRECT: Map only allowed fields
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    // Never set: user.setRole(), user.setAdmin(), etc.
    return UserDTO.from(userRepository.save(user));
}

// Safe redirect validation
public String validateRedirectUrl(String url) {
    List<String> allowedDomains = List.of("app.example.com", "www.example.com");
    try {
        URI uri = new URI(url);
        if (!allowedDomains.contains(uri.getHost())) {
            return "/dashboard";  // Default safe redirect
        }
        return url;
    } catch (URISyntaxException ex) {
        return "/dashboard";
    }
}
```

---

## Practice Topics
- Implement JWT authentication with refresh token rotation
- Add method-level security with custom expressions
- Set up rate limiting with Bucket4j
- Implement AES-GCM encryption for PII fields
- Add audit logging with Spring AOP
- Configure all OWASP-recommended security headers
- Write tests for authentication and authorization scenarios
