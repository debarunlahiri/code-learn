# Production LLM, RAG, and Agent Systems

This guide goes beyond model definitions and focuses on designing, evaluating, and operating generative-AI applications in production.

## Start with the simplest solution

Choose the least complex approach that meets the requirement:

1. Use deterministic code or search when the expected output is fully specified.
2. Use prompting when a general model already knows the task and only needs instructions or examples.
3. Add RAG when answers need private, changing, or citable knowledge.
4. Fine-tune when repeated examples must change behavior, terminology, style, or task accuracy.
5. Use an agent only when the system must choose tools or adapt its plan at runtime.

Complexity increases evaluation effort, latency, cost, and the number of failure modes. Establish a measurable baseline before adding another layer.

## Prompt and context design

A production prompt should separate trusted instructions from untrusted data. It normally contains the role, task, constraints, output schema, examples, and fallback behavior. Put dynamic content inside clearly marked fields rather than concatenating it into instructions.

### Practical controls

- Request structured output with a schema and validate it after generation.
- State what the model should do when information is missing.
- Include only examples that represent real edge cases.
- Keep business rules in code when they must always hold.
- Version prompts alongside code and record the version on every request.
- Test prompt changes against a fixed regression dataset.

Lower temperature usually makes output more repeatable, but it does not guarantee correctness. A fixed seed, where supported, can help debugging but should not replace tests.

## Context-window management

More context is not always better. Long inputs increase cost and latency and may bury important evidence. Allocate a context budget across system instructions, conversation history, retrieved evidence, tool results, and expected output.

Summarize old conversation turns only when their exact wording is not important. Preserve user decisions, constraints, identifiers, and unresolved questions as structured state. Never silently truncate required evidence.

## A production RAG pipeline

### 1. Ingestion

- Fetch documents incrementally and make retries idempotent.
- Extract headings, tables, lists, and page references where possible.
- Remove boilerplate and detect duplicate or near-duplicate content.
- Attach document ID, version, timestamp, source, tenant, and access metadata.
- Quarantine documents that fail parsing instead of indexing corrupt text.

### 2. Chunking

Chunk by semantic boundaries such as sections, paragraphs, or procedures. Fixed token windows are a useful baseline but may split a definition from its explanation. Small chunks improve retrieval precision; larger chunks preserve context. Evaluate the trade-off on real questions.

Store a stable chunk ID and enough surrounding metadata to reconstruct citations. Overlap can protect boundary information, but excessive overlap creates duplicates in the retrieved context.

### 3. Indexing and retrieval

Vector search captures semantic similarity, while keyword search handles exact names, codes, and uncommon terms. Hybrid retrieval often combines both. Apply tenant and access-control filters during retrieval, not after generation.

A typical sequence is:

`query rewrite -> filtered hybrid search -> candidate fusion -> reranking -> context assembly`

Use query rewriting carefully because a rewrite can remove an important constraint. Retain the original query and compare performance with and without rewriting.

### 4. Reranking

A reranker scores the query and each candidate together, which is generally more precise but more expensive than embedding similarity. Retrieve a broad candidate set cheaply, rerank it, and send only the best non-redundant evidence to the generator.

### 5. Generation and citations

Tell the model to answer from supplied evidence and abstain when evidence is insufficient. Citation markers should map to source metadata controlled by the application. Verify that cited passages support the associated claims; merely producing citation-shaped text is not groundedness.

## Evaluating RAG correctly

Evaluate retrieval and generation separately so a poor answer can be diagnosed.

### Retrieval metrics

- `Recall@k`: whether a relevant chunk appears in the first `k` results.
- `Precision@k`: how many of the first `k` results are relevant.
- `MRR`: rewards placing the first relevant result near the top.
- `nDCG`: measures ranking quality when relevance has multiple grades.

### Answer metrics

- Correctness: does the response answer the question accurately?
- Groundedness: is each factual claim supported by retrieved evidence?
- Completeness: are all requested parts addressed?
- Citation precision and recall: are citations valid and are important claims cited?
- Abstention quality: does the system refuse when evidence is inadequate?
- Latency, token usage, and cost per successful answer.

Create a versioned test set from real, synthetic, adversarial, ambiguous, and unanswerable questions. Report results by category, source, language, and difficulty rather than only one average.

## Model-based evaluation

An LLM judge can scale subjective evaluation but may prefer verbose answers, familiar phrasing, or its own model family. Give the judge a precise rubric, randomize answer order in pairwise tests, require evidence for scores, and calibrate it against human labels.

Use deterministic validators for schemas, citations, calculations, forbidden content, and exact business rules. Reserve human review for subjective quality, high-risk decisions, and periodic judge calibration.

## Tool calling

A tool definition needs a narrow purpose, typed parameters, useful descriptions, and explicit error behavior. Validate every argument server-side; the model output is not trusted input.

Tools that write data, spend money, contact people, or delete resources should use least privilege and an approval boundary. Prefer idempotency keys for retryable writes. Return compact structured results, because dumping a large raw response into context wastes tokens and can introduce untrusted instructions.

## Workflows versus agents

A workflow follows a predefined graph. An agent chooses the next action dynamically. Use a workflow for stable processes such as classify, retrieve, draft, validate, and publish. Use an agent when the required sequence cannot be known in advance, such as investigating an unfamiliar operational incident with several diagnostic tools.

Every agent should have:

- A defined goal and termination condition
- An allowlist of tools and resources
- Maximum steps, time, and cost
- Validation of tool inputs and outputs
- Loop and duplicate-action detection
- Checkpoints for consequential actions
- A trace of decisions, tool calls, results, and model versions

Multi-agent designs add coordination overhead and are not automatically more capable. Introduce them only when roles have genuinely different context, permissions, or parallelizable work.

## Memory

Conversation history is not the same as memory. Useful memory types include:

- Working memory: state needed during the current task.
- Episodic memory: summaries of past interactions or outcomes.
- Semantic memory: stable facts and preferences.
- Procedural memory: reusable instructions or successful plans.

Store only data that has a clear future use. Record provenance, scope it to the correct user or tenant, apply retention rules, and allow correction or deletion. Retrieved memory is untrusted context and can be stale, so distinguish it from current user instructions.

## Security threats and controls

### Prompt injection

An attacker may place instructions in a user message, web page, email, or retrieved document. Delimit external content, label it as data, restrict tools independently of the prompt, and require approval for high-impact actions. No prompt wording alone provides a complete defense.

### Data leakage

Remove unnecessary secrets and personal data before requests. Enforce tenant isolation in storage, retrieval, caches, logs, and evaluation datasets. Do not cache responses across authorization boundaries unless the cache key includes the relevant identity and permissions.

### Unsafe output

Apply output validation and domain-specific policy checks before executing or displaying a response. Escape generated content for its destination to reduce SQL injection, shell injection, cross-site scripting, and similar risks.

### Excessive agency

Limit permissions, budgets, and reachable systems. Separate read tools from write tools. Present the exact proposed action to a human when approval is required rather than asking for vague consent.

## Reliability and observability

Trace each request across retrieval, reranking, generation, validation, and tool execution. Useful fields include request ID, tenant, prompt version, model, token counts, retrieved chunk IDs and scores, tool latency, retry count, outcome, and safety decisions. Redact sensitive content before logging.

Monitor p50, p95, and p99 latency; error and timeout rates; schema failures; retrieval quality; abstention rate; tool success; user feedback; tokens; and cost. Alert on changes by model or prompt version so regressions can be isolated.

Use bounded retries with backoff for transient failures. Do not retry invalid requests or repeat non-idempotent actions blindly. Define fallbacks such as a smaller model, search-only results, cached safe responses, or a clear temporary-failure message.

## Latency and cost optimization

Optimize only after measuring a representative trace. Common techniques include:

- Route simple and complex requests to different models.
- Reduce repeated instructions and irrelevant retrieved context.
- Cache embeddings, retrieval results, or safe deterministic prefixes.
- Run independent retrieval or tool calls concurrently.
- Stream responses when it improves perceived latency.
- Batch offline inference and embedding jobs.
- Use smaller models for classification, extraction, and validation.
- Set output limits and stop conditions.

Track cost per successful task, not only cost per request. A cheaper request that causes more retries or human correction may be more expensive overall.

## Production rollout

1. Build a deterministic or single-prompt baseline.
2. Create offline evaluation cases and acceptance thresholds.
3. Run shadow traffic without affecting users.
4. Release to internal users or a small canary group.
5. Compare quality, safety, latency, and cost with the baseline.
6. Add an immediate rollback path for model, prompt, index, and tool changes.
7. Review failures and add them to the regression set.

Pin model versions where possible. A model-provider update is a dependency change and should pass the same evaluation gates as a code deployment.

## System-design interview example

### Design an internal support assistant

First clarify document volume, query rate, latency, languages, freshness, permission model, and acceptable failure behavior. Ingest approved sources incrementally, preserve document ACLs, chunk by structure, and build hybrid indexes. At query time, authenticate the user, filter by ACL, retrieve and rerank candidates, generate a cited answer, and abstain when evidence is weak.

Measure retrieval recall, grounded answer quality, citation validity, p95 latency, cost, and unsafe-answer rate. Log document and prompt versions for reproducibility. Roll out with shadow tests and a canary, retain keyword search as a fallback, and never expose a document the user could not access directly.

## Interview questions with concise answers

### Why can RAG still hallucinate?

Retrieval may return irrelevant evidence, useful evidence may be missing, or the generator may ignore or misinterpret correct context. Diagnose retrieval and generation independently and validate claim-to-source support.

### When should you fine-tune instead of using RAG?

Fine-tune when the goal is consistent behavior, format, terminology, or specialized task performance learned from examples. Use RAG for changing or private facts that need provenance.

### How do you choose chunk size?

Start from document structure and the amount of context needed to answer a question. Sweep several sizes and overlaps, then compare retrieval recall, answer quality, latency, and cost on representative queries.

### How do you prevent duplicate tool actions?

Use idempotency keys, persist action state, detect repeated calls, cap iterations, and require confirmation for consequential writes.

### What should happen when the model provider is unavailable?

Use timeouts and a circuit breaker, then route to an approved fallback model or reduced-capability path. Preserve a clear error response when no safe fallback exists and avoid retry storms.

### What is the most important agent metric?

Task success under explicit quality, safety, time, and cost constraints. Step-level metrics help diagnosis but do not prove that the overall task was completed correctly.

## Final design checklist

- Is generative AI necessary for this requirement?
- Are success and unacceptable failure measurable?
- Are prompts, models, indexes, and evaluation sets versioned?
- Are retrieval permissions enforced before generation?
- Are outputs and tool arguments validated?
- Can the system abstain or fall back safely?
- Are consequential actions bounded by approval and least privilege?
- Can a request be reproduced from traces without logging secrets?
- Are quality, safety, latency, and cost tested before rollout?
- Is rollback immediate and is every production failure converted into a test?
