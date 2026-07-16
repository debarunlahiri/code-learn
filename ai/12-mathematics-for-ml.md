# Mathematics for Machine Learning

This guide focuses on intuition first and formulas second. The goal is to understand what an algorithm is optimizing, why it behaves in a particular way, and when its assumptions fail.

## Vectors and matrices

A vector is an ordered collection of values. A customer can be represented as a feature vector such as `[age, income, transaction_count]`. A matrix is a collection of vectors arranged in rows and columns. In supervised learning, rows usually represent observations and columns represent features.

The dot product of two vectors is:

`a · b = Σ a_i b_i`

Geometrically, `a · b = ||a|| ||b|| cos(θ)`. The dot product is large when vectors point in similar directions. This explains why normalized dot products, or cosine similarity, are useful for comparing embeddings.

Matrix multiplication combines linear transformations. A dense neural-network layer calculates `z = XW + b`, where `X` contains input examples, `W` contains learned weights, and `b` is a bias vector. The activation function then introduces non-linearity.

## Derivatives and gradients

A derivative measures how much an output changes when one input changes. For a function with many parameters, the gradient is a vector of partial derivatives. It points in the direction of steepest increase, so gradient descent moves in the opposite direction:

`θ_new = θ_old - η∇L(θ)`

Here, `η` is the learning rate and `L` is the loss. A very small learning rate makes training slow. A very large rate may overshoot the minimum or cause divergence. Adaptive optimizers alter effective learning rates per parameter, but they do not remove the need for tuning.

### Chain rule and backpropagation

A neural network is a composition of functions. The chain rule propagates the effect of a parameter through each intermediate operation. Backpropagation efficiently reuses these intermediate derivatives instead of recalculating every path separately.

For `y = f(g(x))`:

`dy/dx = (df/dg)(dg/dx)`

Automatic differentiation frameworks construct a computation graph during the forward pass and traverse it backward to calculate gradients.

## Probability

Probability represents uncertainty. A random variable maps an outcome to a value. A probability distribution describes the likelihood of possible values.

### Conditional probability

`P(A|B)` means the probability of `A` after observing `B`. Bayes theorem reverses a conditional relationship:

`P(A|B) = P(B|A)P(A) / P(B)`

For fraud detection, `P(fraud|alert)` depends not only on how often fraud triggers an alert but also on the base rate of fraud. Even a strong detector can have modest precision when the positive event is extremely rare.

### Expectation and variance

Expectation is the long-run average of a random variable. Variance measures squared deviation from the mean. Standard deviation is the square root of variance and returns to the original units.

High variance in a learned model means predictions can change substantially with a different training sample. This concept is related to, but distinct from, variance of a data distribution.

## Common distributions

- **Bernoulli:** a single binary outcome, such as conversion or no conversion.
- **Binomial:** number of successes in a fixed number of Bernoulli trials.
- **Normal:** symmetric continuous distribution described by mean and variance.
- **Poisson:** count of events in a fixed interval under simplifying independence assumptions.
- **Exponential:** waiting time between Poisson events.

Real data need not exactly follow these distributions. They are models that can be useful when their assumptions are approximately appropriate.

## Maximum likelihood

Maximum likelihood estimation selects parameters that make the observed data most probable. Products of many small probabilities are numerically unstable, so implementations maximize log-likelihood. Logarithms turn products into sums without changing the optimum.

Binary cross-entropy is the negative log-likelihood for a Bernoulli outcome:

`L = -[y log(p) + (1-y) log(1-p)]`

It strongly penalizes confident incorrect predictions. Mean squared error corresponds to maximum likelihood under normally distributed errors with constant variance.

## Entropy and information gain

Entropy measures uncertainty in a probability distribution:

`H(Y) = -Σ p(y) log p(y)`

A pure classification node has low entropy; a mixed node has high entropy. A decision tree selects splits that reduce weighted child entropy relative to the parent. Information gain is this reduction.

Cross-entropy measures how well a predicted distribution matches the observed distribution. KL divergence measures the additional information used when one distribution approximates another. KL divergence is asymmetric, so it is not a true distance.

## Optimization concepts

### Convex and non-convex objectives

A convex objective has no poor local minimum, which makes optimization more predictable. Linear and logistic regression with common regularization are convex. Neural networks are non-convex; training can encounter saddle points and many functionally similar solutions.

### Batch, stochastic, and mini-batch gradient descent

- Batch gradient descent uses the entire dataset for each update. It is stable but expensive.
- Stochastic gradient descent uses one example. It is noisy but can update quickly.
- Mini-batch gradient descent balances efficient vectorized compute with useful gradient noise.

### Learning-rate schedules

Warmup gradually increases the learning rate at the beginning. Step decay, cosine decay, and performance-based reduction lower it later. Warmup is especially useful for large transformer training where early updates can be unstable.

## Statistical estimation

A sample statistic estimates a population parameter. Estimates vary across samples, so report uncertainty rather than only a point estimate. Bootstrap sampling repeatedly draws observations with replacement and can estimate confidence intervals without a simple analytic formula.

### Hypothesis testing

The null hypothesis describes a default assumption. A p-value is the probability, assuming the null, of observing a result at least as extreme. It does not state the probability that the null is correct. Always combine significance with effect size, confidence intervals, sample quality, and the cost of wrong decisions.

### Multiple comparisons

Testing many hypotheses increases the chance of a false discovery. Bonferroni controls family-wise error conservatively. Benjamini-Hochberg controls the expected false discovery rate and may be more suitable when screening many features or experiments.

## Regularization from a mathematical view

Regularization adds a complexity penalty:

`objective = data_loss + λ × penalty`

L2 regularization corresponds to a Gaussian prior on weights in a Bayesian interpretation. L1 corresponds to a Laplace prior and encourages exact zeros. Increasing `λ` strengthens shrinkage, usually raising bias and reducing variance.

## Numerical stability

Floating-point arithmetic has limited range and precision. Common protections include:

- Subtracting the maximum logit before softmax.
- Using log-sum-exp instead of exponentiating large values directly.
- Adding a small epsilon before division or logarithms.
- Scaling input features to avoid badly conditioned optimization.
- Using stable library functions instead of handwritten probability expressions.

These are correctness concerns, not merely performance improvements.

---

## Complete AI/ML mathematics syllabus

The following sections form a structured reference. For each topic, learn the meaning, the formula, the assumptions behind it, and one place it appears in an ML system.

## 1. Basic mathematics

### Fractions, decimals, and percentages

A fraction `a/b` represents division, a decimal is its base-10 representation, and a percentage is a fraction out of 100. Convert a probability such as `0.82` to `82%` by multiplying by 100. Percentage change is:

`percentage change = (new - old) / old × 100`

Do not confuse percentage points with percent change. Increasing accuracy from 80% to 84% is an increase of 4 percentage points, or a relative increase of 5%.

### Exponents and logarithms

An exponent represents repeated multiplication: `a^m × a^n = a^(m+n)` and `(a^m)^n = a^(mn)`. Negative powers give reciprocals, so `a^(-n) = 1/a^n`.

A logarithm reverses exponentiation: `log_b(x) = y` means `b^y = x`. Important rules are:

- `log(ab) = log(a) + log(b)`
- `log(a/b) = log(a) - log(b)`
- `log(a^k) = k log(a)`

Logarithms convert probability products into sums, improve numerical stability, and appear in cross-entropy, likelihood, information theory, and log-scaled features.

### Functions and graphs

A function maps an input to one output: `f: X -> Y`. Its domain is the allowed input set and its range is the produced output set. A graph reveals slope, intercepts, curvature, discontinuities, and asymptotic behavior.

Linear functions have the form `f(x) = mx + c`. Polynomial, exponential, logarithmic, piecewise, and periodic functions model different relationships. In ML, a model itself is a parameterized function `f(x; θ)`.

### Summation notation

Sigma notation compresses repeated addition:

`Σ(i=1 to n) x_i = x_1 + x_2 + ... + x_n`

The sample mean is `x̄ = (1/n)Σx_i`, and mean squared error is `(1/n)Σ(y_i - ŷ_i)^2`. Constants can be moved outside a sum, and sums can be split term by term.

### Equations and inequalities

An equation states that two expressions are equal. Apply the same valid operation to both sides when solving it. An inequality uses `<`, `>`, `≤`, or `≥`; multiplying or dividing by a negative number reverses its direction.

Constraints in optimization are often inequalities, such as `w_i ≥ 0` or `||w||₂ ≤ c`.

## 2. Linear algebra

### Scalars, vectors, and matrices

A scalar is one number, a vector is an ordered one-dimensional array, and a matrix is a rectangular two-dimensional array. A tensor generalizes these ideas to more dimensions. If `X` has shape `m × n`, it commonly represents `m` observations and `n` features.

Shapes must be compatible. If `A` is `m × n` and `B` is `n × p`, then `AB` exists and has shape `m × p`.

### Vector operations

Vectors of the same dimension are added component-wise. Scalar multiplication scales every component. Element-wise multiplication, or the Hadamard product, is different from a dot product:

- Hadamard: `a ⊙ b = [a₁b₁, ..., aₙbₙ]`
- Dot product: `a · b = Σaᵢbᵢ`, producing a scalar

The dot product measures alignment and implements weighted sums. If vectors are normalized, their dot product equals cosine similarity.

### Matrix operations and transpose

Matrix addition is component-wise and requires equal shapes. Matrix multiplication combines rows of the first matrix with columns of the second. It is associative but generally not commutative: `AB ≠ BA`.

The transpose swaps rows and columns: `(Aᵀ)ᵢⱼ = Aⱼᵢ`. Useful identities include `(AB)ᵀ = BᵀAᵀ` and `(Aᵀ)ᵀ = A`.

### Identity and inverse matrices

The identity matrix `I` has ones on the diagonal and satisfies `AI = IA = A`. An inverse `A⁻¹` satisfies `AA⁻¹ = I`, but it exists only for a square, full-rank matrix.

Numerically, solve `Ax = b` using a stable factorization rather than explicitly calculating `A⁻¹`. Near-singular matrices amplify numerical error.

### Determinant and rank

The determinant measures signed volume scaling under a square linear transformation. `det(A) = 0` means the transformation collapses at least one dimension and the matrix is singular.

Rank is the number of linearly independent rows or columns. A low-rank matrix contains redundant directions. Rank affects identifiability in regression and motivates low-rank compression and matrix factorization.

### Linear transformations and equation systems

A linear transformation satisfies additivity and scalar compatibility and can be represented as `y = Ax`. Rotation, projection, scaling, and reflection are examples. A system `Ax = b` may have one solution, no solution, or infinitely many solutions depending on rank and consistency.

Least squares finds the vector minimizing `||Ax - b||₂²` when no exact solution exists. The normal equation is `x = (AᵀA)⁻¹Aᵀb`, although QR or SVD is usually more stable in software.

### Eigenvalues and eigenvectors

An eigenvector keeps its direction under a transformation:

`Av = λv`

The eigenvalue `λ` gives the scale factor. Eigenvectors reveal principal directions, long-term dynamics, graph structure, and covariance directions. They exist only for square matrices, though complex eigenvalues may occur.

### Orthogonality and orthonormality

Vectors are orthogonal when their dot product is zero. Orthonormal vectors are orthogonal and each has unit length. For a matrix `Q` with orthonormal columns, `QᵀQ = I`; multiplication by `Q` preserves lengths and angles.

### Vector spaces, basis, and dimension

A vector space is closed under vector addition and scalar multiplication. A basis is a linearly independent set that spans the space. The number of basis vectors is its dimension. Coordinates are coefficients relative to a chosen basis.

The column space contains all outputs `Ax`; the null space contains vectors mapped to zero. The rank-nullity theorem states `rank(A) + nullity(A) = number of columns`.

### Matrix decomposition

Factorizations expose structure and make computations stable:

- LU decomposes a matrix into lower and upper triangular factors.
- QR writes `A = QR`, with orthonormal `Q`; it is useful for least squares.
- Eigendecomposition writes a diagonalizable square matrix as `A = VΛV⁻¹`.
- Cholesky writes a symmetric positive-definite matrix as `A = LLᵀ`.
- SVD writes any `m × n` matrix as `A = UΣVᵀ`.

### Singular Value Decomposition

SVD orders directions by singular value. Keeping the largest `k` singular values gives the best rank-`k` approximation under common matrix norms. Uses include compression, denoising, pseudoinverses, latent semantic analysis, and recommender systems.

### PCA mathematics

PCA finds orthogonal directions of maximum variance. Center the data, compute its covariance matrix, and select eigenvectors with the largest eigenvalues. Equivalently, apply SVD to the centered data matrix.

The explained-variance ratio of component `i` is `λᵢ / Σλⱼ`. PCA is sensitive to feature scale, so standardization is often necessary. It is a linear projection and may not preserve nonlinear structure.

### Norms

A norm measures magnitude:

- `L1: ||x||₁ = Σ|xᵢ|`
- `L2: ||x||₂ = sqrt(Σxᵢ²)`
- `L∞: ||x||∞ = max|xᵢ|`
- Frobenius matrix norm: `||A||F = sqrt(Σᵢⱼaᵢⱼ²)`

L1 penalties encourage sparse parameters; L2 penalties smoothly shrink them. Norm choice also defines distance and robustness assumptions.

## 3. Probability

### Experiments, sample spaces, and events

A random experiment has an uncertain outcome. The sample space `Ω` is the set of all possible outcomes, and an event is a subset of `Ω`. For a die, `Ω = {1,2,3,4,5,6}` and “even” is `{2,4,6}`.

Probability obeys `0 ≤ P(A) ≤ 1`, `P(Ω) = 1`, and additivity for mutually exclusive events. The complement rule is `P(Aᶜ) = 1 - P(A)`. The general addition rule is:

`P(A ∪ B) = P(A) + P(B) - P(A ∩ B)`

### Conditional probability and independence

Conditional probability is `P(A|B) = P(A ∩ B)/P(B)`. Two events are independent when `P(A ∩ B) = P(A)P(B)`, equivalently `P(A|B) = P(A)` when defined. Mutually exclusive non-impossible events are not independent.

The multiplication rule is `P(A ∩ B) = P(A|B)P(B)`. Bayes' theorem is:

`P(A|B) = P(B|A)P(A)/P(B)`

The prior `P(A)` is updated through the likelihood `P(B|A)` to form the posterior `P(A|B)`.

### Random variables

A random variable assigns a number to each outcome. A discrete variable has countable values and uses a probability mass function (PMF). A continuous variable uses a probability density function (PDF); probability is area under the density, not the density value at one point.

The cumulative distribution function is `F(x) = P(X ≤ x)`. It applies to both discrete and continuous variables and never decreases.

### Joint, marginal, and conditional probability

A joint distribution describes multiple variables together. Marginalization removes a variable by summing or integrating: `P(X=x) = ΣᵧP(X=x,Y=y)`. Conditional probability divides a joint probability by the relevant marginal.

These operations underlie graphical models, Naive Bayes, latent-variable models, and probabilistic inference.

### Expectation, variance, covariance, and correlation

For a discrete variable, `E[X] = ΣxP(X=x)`. Linearity gives `E[aX+bY] = aE[X]+bE[Y]` even without independence.

`Var(X) = E[(X-μ)²] = E[X²]-μ²`

Covariance is `Cov(X,Y) = E[(X-E[X])(Y-E[Y])]`. Correlation normalizes it to `ρ = Cov(X,Y)/(σXσY)`, which lies between -1 and 1. Zero correlation does not generally imply independence, and correlation does not prove causation.

### Law of large numbers and central limit theorem

The law of large numbers says the sample mean converges toward the expected value as sample size grows. The central limit theorem says that, under suitable conditions, the standardized sum or mean approaches a normal distribution. This supports many confidence intervals and tests, but small, dependent, or heavy-tailed samples may violate the approximation.

## 4. Probability distributions

| Distribution | Type | Parameters | Mean | Variance | Typical ML use |
|---|---|---|---|---|---|
| Bernoulli | Discrete | `p` | `p` | `p(1-p)` | One binary outcome |
| Binomial | Discrete | `n, p` | `np` | `np(1-p)` | Success count in `n` trials |
| Multinomial | Discrete | `n, p₁...pₖ` | `npᵢ` per class | Depends on class | Multi-class counts |
| Uniform | Either | Bounds | Midpoint | Continuous: `(b-a)²/12` | Random initialization, simulation |
| Normal/Gaussian | Continuous | `μ, σ²` | `μ` | `σ²` | Noise and residual models |
| Poisson | Discrete | `λ` | `λ` | `λ` | Event counts per interval |
| Exponential | Continuous | `λ` | `1/λ` | `1/λ²` | Waiting time between Poisson events |

“Normal” and “Gaussian” name the same distribution. Its PDF is:

`f(x) = [1/(σsqrt(2π))] exp(-(x-μ)²/(2σ²))`

A PMF gives probabilities directly for discrete outcomes. A PDF must be integrated over an interval. A CDF returns probability up to a threshold. Distribution assumptions should be checked rather than inferred from a familiar histogram shape.

## 5. Statistics

### Descriptive statistics

The mean uses every value but is sensitive to outliers. The median is the middle ordered value and is robust to extreme observations. The mode is the most frequent value. Range is `max-min`; percentiles state the value below which a percentage of observations falls.

Variance and standard deviation describe spread. Covariance and correlation describe pairwise linear association. Always visualize the distribution because identical summaries can hide very different shapes.

### Population, sample, and sampling

A population is the entire group of interest; a sample is the observed subset. Random sampling reduces selection bias. Stratified sampling preserves important subgroup proportions, cluster sampling selects groups, and systematic sampling selects at regular intervals.

Convenience and survivorship sampling can produce biased conclusions. More data reduces random error but does not repair systematic bias.

### Inferential statistics and confidence intervals

Inferential statistics uses a sample to reason about a population. A confidence interval has the form:

`estimate ± critical value × standard error`

A 95% frequentist confidence procedure captures the true parameter in 95% of repeated samples; it does not mean a fixed computed interval has a 95% probability of containing that parameter.

### Hypothesis tests and p-values

Define a null hypothesis `H₀` and alternative `H₁`, choose a statistic before examining results, and compute how unusual the observation would be under `H₀`. The p-value is `P(result at least this extreme | H₀)`, not `P(H₀ is true | data)`.

Statistical significance is not practical significance. Report effect size, uncertainty, assumptions, and test power.

### Common tests

- Z-test: means or proportions when sampling behavior is known or well approximated and sample size is sufficiently large.
- T-test: compares means when population variance is unknown; paired and independent variants answer different questions.
- Chi-square test: checks categorical independence or goodness of fit using expected counts.
- ANOVA: tests whether at least one of several group means differs by comparing between-group and within-group variation.

Check independence, distribution, variance, and sample-size assumptions. Use non-parametric or resampling alternatives when assumptions are inappropriate.

### Type I and Type II errors

A Type I error is a false positive: rejecting a true null, with probability `α`. A Type II error is a false negative: failing to reject a false null, with probability `β`. Power is `1-β`. Lowering `α` without increasing sample size often lowers power.

### MLE and MAP

Maximum likelihood estimation chooses:

`θ_MLE = argmaxθ P(D|θ)`

Maximum a posteriori estimation includes a prior:

`θ_MAP = argmaxθ P(D|θ)P(θ)`

MAP can be viewed as regularized likelihood. With abundant data, the likelihood often dominates the prior; with limited data, the prior can materially stabilize estimates.

## 6. Calculus

### Limits and continuity

A limit describes the value a function approaches. A function is continuous at `a` when `lim(x→a)f(x) = f(a)`. Derivatives require local limiting behavior, while discontinuities and sharp corners need special treatment.

### Derivatives and rules

The derivative is instantaneous rate of change:

`f'(x) = lim(h→0)[f(x+h)-f(x)]/h`

Useful rules include `(uv)' = u'v + uv'` and the chain rule `(f(g(x)))' = f'(g(x))g'(x)`. Partial derivatives change one variable while holding others fixed.

### Gradient and directional derivative

The gradient `∇f` collects all partial derivatives and points toward steepest local increase. The directional derivative along unit vector `u` is `Dᵤf = ∇f · u`. Gradient descent follows `-∇f`.

### Jacobian and Hessian

The Jacobian contains first derivatives of a vector-valued function: `Jᵢⱼ = ∂fᵢ/∂xⱼ`. It appears in backpropagation and transformations.

The Hessian contains second derivatives of a scalar function: `Hᵢⱼ = ∂²f/(∂xᵢ∂xⱼ)`. Its eigenvalues describe local curvature. A positive-definite Hessian indicates a strict local minimum at a stationary point.

### Integration

Integration accumulates quantities and gives area under a curve. For continuous probability, `P(a≤X≤b) = ∫ₐᵇf(x)dx`. The fundamental theorem connects differentiation and integration.

Multivariable integrals marginalize joint densities and compute expectations, though high-dimensional integrals often require sampling or approximation.

### Extrema and convexity

At an interior differentiable local extremum, the gradient is normally zero. That condition can also identify a maximum or saddle point, so curvature or surrounding values matter.

A convex function satisfies `f(tx+(1-t)y) ≤ tf(x)+(1-t)f(y)` for `0≤t≤1`. Every local minimum of a convex function is global. Neural-network objectives are generally non-convex.

## 7. Optimization

### Objectives, costs, and losses

A loss measures error for one example; a cost or objective usually aggregates loss and may add regularization. Training solves `minθ J(θ)`. The metric used for business success need not be differentiable, so the training loss is often a tractable surrogate.

### Gradient-descent variants

- Batch gradient descent computes an exact dataset gradient per step but is expensive.
- SGD uses one example, producing cheap but noisy updates.
- Mini-batch descent uses a small set and is the standard compromise for accelerators.

The learning rate controls update size. Momentum maintains a velocity to smooth noise and accelerate consistent directions. RMSProp scales updates using an exponential average of squared gradients. Adam combines momentum-like first moments with RMS-scaled second moments and bias correction.

### Constrained optimization and Lagrange multipliers

Constrained problems optimize subject to equalities or inequalities. For equality constraint `g(x)=0`, form:

`L(x, λ) = f(x) + λg(x)`

At a regular optimum, `∇f = -λ∇g`. Inequality constraints require complementary conditions summarized by the Karush-Kuhn-Tucker conditions.

### Local and global minima

A local minimum is best only in a neighborhood; a global minimum is best over the entire feasible set. Convexity makes local minima global. Non-convex optimization can also contain saddle points and flat regions.

### Regularization

Regularization constrains effective model complexity:

`J(θ) = data_loss + λR(θ)`

L1 uses `R(w)=Σ|wᵢ|` and promotes sparsity. L2 uses `R(w)=Σwᵢ²` and smoothly shrinks weights. Regularization also includes early stopping, dropout, augmentation, and explicit constraints.

## 8. Information theory

### Entropy and cross-entropy

Entropy `H(P) = -Σp(x)log p(x)` measures average uncertainty. Cross-entropy `H(P,Q) = -Σp(x)log q(x)` measures coding cost when data follows `P` but predictions use `Q`.

For a fixed target distribution, minimizing cross-entropy also minimizes KL divergence because `H(P,Q) = H(P) + D_KL(P||Q)`.

### Information gain and mutual information

Information gain is the entropy reduction after observing a feature or applying a split. Mutual information is:

`I(X;Y) = Σₓ,ᵧp(x,y)log[p(x,y)/(p(x)p(y))]`

It is zero for independent variables and can capture nonlinear dependence, unlike Pearson correlation.

### KL divergence

`D_KL(P||Q) = Σp(x)log[p(x)/q(x)]`

It is non-negative but asymmetric and does not obey the triangle inequality. It appears in variational inference, knowledge distillation, and distribution alignment.

### Gini impurity and perplexity

Gini impurity is `1-Σpᵢ²`. It is zero for a pure node and is a common decision-tree split criterion.

Perplexity is the exponential of average negative log-likelihood. Lower perplexity means the model assigns higher probability to observed tokens, but it does not directly measure factuality, safety, or usefulness.

## 9. Mathematics for ML algorithms

### Linear regression

The model is `ŷ = Xw + b`. Ordinary least squares minimizes `Σ(y-ŷ)²`. Squared error yields a convex objective and strongly penalizes large residuals. Its statistical interpretation commonly assumes linearity, independent errors, constant error variance, and normally distributed errors for inference.

### Logistic regression

Logistic regression models log-odds linearly: `log[p/(1-p)] = wᵀx+b`. The sigmoid `σ(z)=1/(1+e^-z)` maps the score to `(0,1)`. Binary cross-entropy estimates parameters by Bernoulli maximum likelihood.

### Decision trees

A classification tree selects a split that maximizes impurity reduction. Entropy is `-Σpᵢlog pᵢ`; Gini is `1-Σpᵢ²`. Regression trees often minimize weighted squared error or variance.

### Naive Bayes

Naive Bayes applies `P(y|x) ∝ P(y)∏P(xⱼ|y)` and assumes features are conditionally independent given the class. The assumption is often unrealistic, yet the classifier can work well for sparse text and small data.

### K-nearest neighbours

KNN finds nearby training points under a selected distance and predicts by voting or averaging. Feature scale matters because large-scale dimensions dominate distance. Small `k` has low bias and high variance; large `k` smooths more.

### Support Vector Machines

A linear SVM finds a separating hyperplane with a maximum margin. Soft-margin SVM balances margin width and violations using `C`. The hinge loss is `max(0, 1-yf(x))`.

The kernel trick replaces a dot product with `K(x,z)=φ(x)·φ(z)` without explicitly constructing `φ`. Common kernels include linear, polynomial, and radial basis function.

### K-means

K-means minimizes within-cluster squared Euclidean distance:

`Σ(k) Σ(x in C_k) ||x-μ_k||₂²`

It alternates assignment and centroid-update steps. It assumes roughly compact, similarly scaled clusters and is sensitive to initialization and outliers.

### Bias, variance, and regularization

Bias is systematic error from restrictive assumptions; variance is sensitivity to the training sample. Greater complexity often lowers training bias but raises variance. Regularization, more representative data, ensembling, and validation-based model selection manage the trade-off.

## 10. Mathematics for deep learning

### Layers and activations

A dense layer computes `z = Wa+b`, followed by an activation:

- Sigmoid: `σ(z)=1/(1+e^-z)`, bounded but prone to saturation.
- Tanh: `(e^z-e^-z)/(e^z+e^-z)`, zero-centered but also saturates.
- ReLU: `max(0,z)`, efficient but can produce inactive units.
- Softmax: `pᵢ=e^zᵢ/Σe^zⱼ`, a categorical probability distribution.

Subtract `max(z)` before softmax for numerical stability.

### Loss functions

MSE is `(1/n)Σ(y-ŷ)²` and suits Gaussian-style regression errors. Binary cross-entropy handles Bernoulli outputs. Categorical cross-entropy uses `-Σyᵢlog pᵢ` for mutually exclusive classes.

Choose a loss whose assumptions and error penalties match the task. Class weights or focal loss may help when important examples are rare.

### Forward propagation and backpropagation

Forward propagation calculates predictions and caches intermediate values. Backpropagation applies the chain rule from loss to each parameter. For a layer, an upstream gradient is multiplied by the local derivative, and gradients from multiple downstream paths are added.

### Initialization and normalization

Identical weight initialization prevents hidden units from learning different features. Xavier initialization controls variance for sigmoid or tanh-like activations; He initialization is designed for ReLU-like activations.

Batch normalization standardizes mini-batch activations, then learns scale and shift parameters. It can stabilize optimization and permits larger learning rates, but training and inference use different statistics. Layer normalization is often preferred in transformers.

### Optimization algorithms

SGD with momentum can generalize strongly but needs learning-rate scheduling. Adam converges quickly across differently scaled parameters and is a common default. Optimizer choice cannot rescue incorrect data, loss, or gradients; monitor training and validation curves.

## 11. Distance and similarity measures

| Measure | Formula or idea | Appropriate use and caution |
|---|---|---|
| Euclidean | `sqrt(Σ(xᵢ-yᵢ)²)` | Straight-line distance; sensitive to scale and outliers |
| Manhattan | `Σ|xᵢ-yᵢ|` | Grid-like or sparse spaces; less dominated by one large difference |
| Minkowski | `(Σ|xᵢ-yᵢ|^p)^(1/p)` | Generalizes Manhattan (`p=1`) and Euclidean (`p=2`) |
| Cosine similarity | `(x·y)/(||x||||y||)` | Directional similarity for text and embeddings; ignores magnitude |
| Hamming | Count or fraction of unequal positions | Equal-length binary or categorical strings |
| Jaccard | `|A∩B|/|A∪B|` | Set or binary-feature overlap; ignores joint absence |
| Mahalanobis | `sqrt((x-y)ᵀS⁻¹(x-y))` | Accounts for feature scale and covariance; covariance estimate must be stable |

Standardize numeric features before scale-sensitive distances. In high dimensions, distances may concentrate and nearest neighbours can become less distinctive—the curse of dimensionality.

## Recommended learning order

1. Basic mathematics and notation
2. Linear algebra
3. Probability and probability distributions
4. Descriptive and inferential statistics
5. Single-variable and multivariable calculus
6. Optimization
7. Information theory
8. Mathematics of classical ML algorithms
9. Mathematics of deep learning
10. Distance measures, numerical stability, and integrated practice

At every stage, solve small calculations by hand, then reproduce them with NumPy or a similar library. The goal is to connect symbols to data shapes, model behavior, assumptions, and debugging decisions rather than memorize formulas alone.
