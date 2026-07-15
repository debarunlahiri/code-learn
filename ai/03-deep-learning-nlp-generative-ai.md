# Deep Learning, NLP, and Generative AI

## Neural-network essentials

A network applies parameterized transformations and nonlinear activations. Backpropagation uses the chain rule to compute gradients, and an optimizer updates weights. ReLU is common in hidden layers; sigmoid suits binary outputs; softmax forms a categorical distribution.

### Common training problems

- Vanishing/exploding gradients: use careful initialization, normalization, residual connections, and gradient clipping.
- Overfitting: use augmentation, dropout, weight decay, and early stopping.
- Unstable loss: lower the learning rate, inspect normalization and bad samples, and clip gradients.
- Dead ReLUs: use better initialization or leaky activations.

## CNNs, RNNs, and transformers

CNNs share local filters and are effective for spatial data. RNNs process sequences recurrently; LSTM and GRU gates improve long-range learning. Transformers use self-attention for parallel sequence modelling and have become the standard architecture for language models.

Scaled dot-product attention is:

`Attention(Q, K, V) = softmax(QKᵀ / sqrt(d_k))V`

The scale prevents large dot products from making softmax gradients too small. Multi-head attention learns different relationships in parallel.

## Embeddings

Embeddings map items to dense vectors. Cosine similarity compares direction and is common for semantic retrieval. Validate embeddings on the actual domain, language, chunk size, and retrieval task. Version the embedding model because switching models invalidates existing vector indexes.

## Retrieval-augmented generation

A typical RAG flow is:

1. Parse and clean documents.
2. Split them into meaningful, slightly overlapping chunks.
3. Embed chunks and store vectors with metadata.
4. Retrieve candidates for a query.
5. Optionally apply filters, hybrid search, and a reranker.
6. Generate an answer grounded in the selected context.
7. Return citations and collect evaluation signals.

Improve weak retrieval before changing the generator. Measure recall@k, mean reciprocal rank, answer correctness, groundedness, citation quality, latency, and cost.

## Fine-tuning versus RAG

Use RAG for current, private, source-grounded knowledge. Use fine-tuning to change behavior, format, terminology, or task performance. Prompting is the cheapest first step. Some systems combine all three.

## Hallucination controls

- Restrict responses to retrieved evidence.
- Ask for abstention when evidence is insufficient.
- Preserve source metadata and display citations.
- Use structured output validation.
- Add deterministic business-rule checks.
- Test adversarial and out-of-domain queries.
- Avoid treating model self-confidence as reliable probability.

## Agentic systems

An agent chooses actions and tools across multiple steps. Use one only when the task genuinely needs dynamic decisions; a deterministic workflow is easier to test for fixed processes. Bound tools with typed schemas, least privilege, iteration limits, timeouts, idempotency keys, and human approval for consequential actions.

## Evaluation

Build a versioned dataset covering normal, difficult, adversarial, multilingual, and failure cases. Combine deterministic checks, task-specific metrics, carefully calibrated model-based grading, and human review. Track quality by slice rather than reporting only an overall average.

## Safety and security

Treat user input and retrieved documents as untrusted. Defend against prompt injection by separating instructions from data, limiting tool permissions, validating output, filtering sensitive content, and logging actions. Remove secrets and personal data before model calls where possible.

