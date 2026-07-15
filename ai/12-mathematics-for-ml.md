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

