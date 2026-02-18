# Real-World Projects in Jetpack Compose

Goal: Apply Compose knowledge to build complete, production-ready applications.

---

## 1. Social Media App Clone

### What it does
Build a Twitter/Instagram-like social media application with posts, likes, and user profiles.

### Why it matters
- Real-world complexity
- Multiple features integration
- User interaction patterns
- Performance considerations

### Intuition
Social media apps are like digital communities - they need to handle real-time updates, user interactions, and smooth scrolling through content.

### When to use
- Portfolio projects
- Learning advanced patterns
- Understanding complex state management
- Performance optimization practice

### Features to Implement
- User authentication
- Post creation with images
- Like/comment system
- User profiles
- Real-time feed updates
- Search functionality

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Data models
data class Post(
    val id: String,
    val author: User,
    val content: String,
    val timestamp: Long,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean,
    val imageUrl: String? = null
)

data class User(
    val id: String,
    val name: String,
    val username: String,
    val avatar: String,
    val bio: String,
    val followers: Int,
    val following: Int,
    val posts: Int
)

// ViewModel for social media app
class SocialMediaViewModel : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadPosts()
        loadCurrentUser()
    }
    
    private fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            // Simulate API call
            kotlinx.coroutines.delay(1000)
            
            _posts.value = listOf(
                Post(
                    id = "1",
                    author = User("1", "John Doe", "@johndoe", "", "Developer", 1000, 500, 50),
                    content = "Just shipped a new feature! 🚀 #android #compose",
                    timestamp = System.currentTimeMillis() - 3600000,
                    likes = 42,
                    comments = 8,
                    isLiked = false
                ),
                Post(
                    id = "2",
                    author = User("2", "Jane Smith", "@janesmith", "", "Designer", 2000, 300, 100),
                    content = "Love the new Material 3 design system! 💙",
                    timestamp = System.currentTimeMillis() - 7200000,
                    likes = 128,
                    comments = 24,
                    isLiked = true
                )
            )
            _isLoading.value = false
        }
    }
    
    private fun loadCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = User(
                id = "current",
                name = "Current User",
                username = "@currentuser",
                avatar = "",
                bio = "Android Developer",
                followers = 500,
                following = 300,
                posts = 25
            )
        }
    }
    
    fun likePost(postId: String) {
        viewModelScope.launch {
            _posts.value = _posts.value.map { post ->
                if (post.id == postId) {
                    post.copy(
                        isLiked = !post.isLiked,
                        likes = if (post.isLiked) post.likes - 1 else post.likes + 1
                    )
                } else {
                    post
                }
            }
        }
    }
    
    fun createPost(content: String) {
        viewModelScope.launch {
            val currentUser = _currentUser.value ?: return@launch
            val newPost = Post(
                id = System.currentTimeMillis().toString(),
                author = currentUser,
                content = content,
                timestamp = System.currentTimeMillis(),
                likes = 0,
                comments = 0,
                isLiked = false
            )
            _posts.value = listOf(newPost) + _posts.value
        }
    }
}

// Main feed screen
@Composable
fun SocialMediaFeedScreen(
    viewModel: SocialMediaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    
    var showCreatePost by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TopAppBar(
            title = { Text("Social Feed") },
            actions = {
                IconButton(onClick = { showCreatePost = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Create post")
                }
            }
        )
        
        // Content
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            posts.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No posts yet")
                }
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(posts) { post ->
                        PostCard(
                            post = post,
                            onLike = { viewModel.likePost(post.id) },
                            onComment = { /* Handle comment */ },
                            onShare = { /* Handle share */ }
                        )
                    }
                }
            }
        }
    }
    
    // Create post dialog
    if (showCreatePost) {
        CreatePostDialog(
            onDismiss = { showCreatePost = false },
            onPost = { content ->
                viewModel.createPost(content)
                showCreatePost = false
            }
        )
    }
}

@Composable
fun PostCard(
    post: Post,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Author info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.author.name.first().toString(),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.author.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "@${post.author.username}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(onClick = { /* Handle more options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Post content
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Post image (if any)
            post.imageUrl?.let { imageUrl ->
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    // In real app, use AsyncImage from Coil
                    Text(
                        text = "Image: $imageUrl",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Engagement metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Like button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onLike() }
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (post.isLiked) "Unlike" else "Like",
                            tint = if (post.isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.likes.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    // Comment button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onComment() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.comments.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    // Share button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onShare() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Text(
                    text = formatTimestamp(post.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CreatePostDialog(
    onDismiss: () -> Unit,
    onPost: (String) -> Unit
) {
    var content by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Post") },
        text = {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("What's on your mind?") },
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (content.isNotBlank()) {
                        onPost(content)
                    }
                },
                enabled = content.isNotBlank()
            ) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> "${diff / 86400000}d ago"
    }
}
```

---

## 2. E-commerce App

### What it does
Build a shopping application with product catalog, cart, and checkout functionality.

### Why it matters
- Complex state management
- Payment integration patterns
- Product listing optimization
- User experience design

### Intuition
E-commerce apps are like digital stores - they need to showcase products beautifully, handle shopping carts efficiently, and provide smooth checkout experiences.

### When to use
- Commercial applications
- Learning complex UI patterns
- Payment integration practice
- Performance optimization

### Features to Implement
- Product catalog with search/filter
- Shopping cart management
- Product details and reviews
- Checkout process
- Order tracking
- User authentication

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.random.Random

// E-commerce data models
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val rating: Float,
    val reviews: Int,
    val inStock: Boolean,
    val imageUrl: String? = null
)

data class CartItem(
    val product: Product,
    val quantity: Int
)

data class Order(
    val id: String,
    val items: List<CartItem>,
    val total: Double,
    val status: OrderStatus,
    val timestamp: Long
)

enum class OrderStatus {
    PENDING, CONFIRMED, SHIPPED, DELIVERED
}

// E-commerce ViewModel
class EcommerceViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()
    
    init {
        loadProducts()
    }
    
    private fun loadProducts() {
        viewModelScope.launch {
            // Simulate product data
            _products.value = listOf(
                Product("1", "Laptop", "High-performance laptop", 999.99, "Electronics", 4.5f, 128, true),
                Product("2", "Phone", "Latest smartphone", 699.99, "Electronics", 4.3f, 256, true),
                Product("3", "Headphones", "Wireless headphones", 149.99, "Electronics", 4.7f, 89, true),
                Product("4", "Book", "Bestseller novel", 19.99, "Books", 4.2f, 45, true),
                Product("5", "Shoes", "Running shoes", 89.99, "Fashion", 4.4f, 167, true),
                Product("6", "Watch", "Smart watch", 299.99, "Electronics", 4.1f, 78, false)
            )
        }
    }
    
    fun addToCart(product: Product) {
        viewModelScope.launch {
            val currentCart = _cart.value.toMutableList()
            val existingItem = currentCart.find { it.product.id == product.id }
            
            if (existingItem != null) {
                val index = currentCart.indexOf(existingItem)
                currentCart[index] = existingItem.copy(quantity = existingItem.quantity + 1)
            } else {
                currentCart.add(CartItem(product, 1))
            }
            
            _cart.value = currentCart
        }
    }
    
    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            _cart.value = _cart.value.filter { it.product.id != productId }
        }
    }
    
    fun updateQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            if (quantity <= 0) {
                removeFromCart(productId)
            } else {
                _cart.value = _cart.value.map { item ->
                    if (item.product.id == productId) {
                        item.copy(quantity = quantity)
                    } else {
                        item
                    }
                }
            }
        }
    }
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }
    
    fun getFilteredProducts(): StateFlow<List<Product>> {
        return combine(_products, _searchQuery, _selectedCategory) { products, query, category ->
            products.filter { product ->
                val matchesQuery = query.isBlank() || 
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
                val matchesCategory = category == "All" || product.category == category
                matchesQuery && matchesCategory
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }
}

// Product catalog screen
@Composable
fun ProductCatalogScreen(
    viewModel: EcommerceViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onProductClick: (Product) -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    val cart by viewModel.cart.collectAsState()
    val filteredProducts by viewModel.getFilteredProducts().collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    
    val categories = remember {
        listOf("All", "Electronics", "Books", "Fashion")
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar with cart
        TopAppBar(
            title = { Text("Shop") },
            actions = {
                IconButton(onClick = onCartClick) {
                    BadgedBox(
                        badge = {
                            if (cart.isNotEmpty()) {
                                Badge {
                                    Text(cart.size.toString())
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            }
        )
        
        // Search and filters
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search products") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Category filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { viewModel.setSelectedCategory(category) },
                        label = { Text(category) }
                    )
                }
            }
        }
        
        // Product grid
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredProducts) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product) },
                    onAddToCart = { viewModel.addToCart(product) }
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Product image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.name.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Product info
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${product.rating} (${product.reviews})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Add to cart button
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(),
                enabled = product.inStock
            ) {
                Text(if (product.inStock) "Add to Cart" else "Out of Stock")
            }
        }
    }
}

// Shopping cart screen
@Composable
fun ShoppingCartScreen(
    viewModel: EcommerceViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onCheckout: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val cart by viewModel.cart.collectAsState()
    val total = cart.sumOf { it.product.price * it.quantity }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TopAppBar(
            title = { Text("Shopping Cart") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        
        if (cart.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your cart is empty",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Cart items
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cart) { cartItem ->
                    CartItemRow(
                        cartItem = cartItem,
                        onQuantityChange = { quantity ->
                            viewModel.updateQuantity(cartItem.product.id, quantity)
                        },
                        onRemove = {
                            viewModel.removeFromCart(cartItem.product.id)
                        }
                    )
                }
            }
            
            // Checkout section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total:",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "$${String.format("%.2f", total)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Proceed to Checkout")
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cartItem.product.name.first().toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Product details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$${cartItem.product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Quantity controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { onQuantityChange(cartItem.quantity - 1) }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                
                Text(
                    text = cartItem.quantity.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                
                IconButton(
                    onClick = { onQuantityChange(cartItem.quantity + 1) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
                
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }
    }
}
```

---

## 3. Task Management App

### What it does
Build a comprehensive task management application with categories, priorities, and due dates.

### Why it matters
- Complex state management
- Data persistence patterns
- User interaction design
- Performance optimization

### Intuition
Task management apps are like digital assistants - they help users organize their work, set priorities, and track progress efficiently.

### When to use
- Productivity applications
- Learning data persistence
- Complex UI patterns
- User experience design

### Features to Implement
- Task creation and editing
- Categories and tags
- Priority levels
- Due date management
- Search and filter
- Progress tracking

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Task management data models
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val priority: Priority,
    val dueDate: LocalDate?,
    val isCompleted: Boolean,
    val createdAt: LocalDate = LocalDate.now()
)

enum class Priority(val value: Int, val color: String) {
    LOW(1, "#4CAF50"),
    MEDIUM(2, "#FF9800"),
    HIGH(3, "#F44336")
}

data class Category(
    val name: String,
    val color: String,
    val icon: String
)

// Task management ViewModel
class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()
    
    private val _showCompleted = MutableStateFlow(true)
    val showCompleted: StateFlow<Boolean> = _showCompleted.asStateFlow()
    
    init {
        loadCategories()
        loadTasks()
    }
    
    private fun loadCategories() {
        _categories.value = listOf(
            Category("Work", "#2196F3", "💼"),
            Category("Personal", "#4CAF50", "🏠"),
            Category("Shopping", "#FF9800", "🛒"),
            Category("Health", "#F44336", "🏥"),
            Category("Learning", "#9C27B0", "📚")
        )
    }
    
    private fun loadTasks() {
        viewModelScope.launch {
            // Simulate task data
            _tasks.value = listOf(
                Task(
                    id = "1",
                    title = "Complete project proposal",
                    description = "Finish the Q4 project proposal and send to team",
                    category = "Work",
                    priority = Priority.HIGH,
                    dueDate = LocalDate.now().plusDays(2),
                    isCompleted = false
                ),
                Task(
                    id = "2",
                    title = "Buy groceries",
                    description = "Milk, eggs, bread, vegetables",
                    category = "Shopping",
                    priority = Priority.MEDIUM,
                    dueDate = LocalDate.now().plusDays(1),
                    isCompleted = false
                ),
                Task(
                    id = "3",
                    title = "Morning workout",
                    description = "30 minutes cardio + strength training",
                    category = "Health",
                    priority = Priority.LOW,
                    dueDate = LocalDate.now(),
                    isCompleted = true
                )
            )
        }
    }
    
    fun addTask(task: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value + task
        }
    }
    
    fun updateTask(task: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.map {
                if (it.id == task.id) task else it
            }
        }
    }
    
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.filter { it.id != taskId }
        }
    }
    
    fun toggleTaskCompletion(taskId: String) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.map { task ->
                if (task.id == taskId) {
                    task.copy(isCompleted = !task.isCompleted)
                } else {
                    task
                }
            }
        }
    }
    
    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }
    
    fun toggleShowCompleted() {
        _showCompleted.value = !_showCompleted.value
    }
    
    fun getFilteredTasks(): StateFlow<List<Task>> {
        return combine(_tasks, _selectedCategory, _showCompleted) { tasks, category, showCompleted ->
            tasks.filter { task ->
                val matchesCategory = category == "All" || task.category == category
                val matchesCompletion = showCompleted || !task.isCompleted
                matchesCategory && matchesCompletion
            }.sortedWith(compareBy<Task> { it.isCompleted }.thenBy { it.priority.value }.reversed())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }
}

// Main task list screen
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onTaskClick: (Task) -> Unit = {},
    onAddTask: () -> Unit = {}
) {
    val categories by viewModel.categories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val showCompleted by viewModel.showCompleted.collectAsState()
    val filteredTasks by viewModel.getFilteredTasks().collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TopAppBar(
            title = { Text("Tasks") },
            actions = {
                IconButton(onClick = onAddTask) {
                    Icon(Icons.Default.Add, contentDescription = "Add task")
                }
            }
        )
        
        // Category filter
        LazyRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("All") + categories.map { it.name }) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { viewModel.setSelectedCategory(category) },
                    label = { Text(category) }
                )
            }
        }
        
        // Show completed toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = showCompleted,
                onCheckedChange = { viewModel.toggleShowCompleted() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Show completed tasks")
        }
        
        // Task list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredTasks) { task ->
                TaskItem(
                    task = task,
                    onClick = { onTaskClick(task) },
                    onToggle = { viewModel.toggleTaskCompletion(task.id) },
                    onDelete = { viewModel.deleteTask(task.id) }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Task content
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Bold,
                        color = if (task.isCompleted) 
                            MaterialTheme.colorScheme.onSurfaceVariant 
                        else 
                            MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Priority indicator
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                Color(android.graphics.Color.parseColor(task.priority.color)),
                                androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
                
                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Category
                    Text(
                        text = task.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    // Due date
                    task.dueDate?.let { dueDate ->
                        Text(
                            text = "Due: ${dueDate.format(DateTimeFormatter.ofPattern("MMM dd"))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (dueDate.isBefore(LocalDate.now())) 
                                MaterialTheme.colorScheme.error 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Delete button
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
```

---

## Practice Projects

### Easy
1. **Simple Weather App** - API integration and basic UI
2. **Calculator App** - State management and calculations
3. **Notes App** - CRUD operations and local storage

### Medium
1. **Recipe App** - Complex layouts and search functionality
2. **Music Player** - Media controls and animations
3. **Chat App** - Real-time messaging and UI

### Hard
1. **Social Media App** - Complete social features
2. **E-commerce Platform** - Shopping cart and payments
3. **Productivity Suite** - Multiple integrated features

---

**Remember**: Real-world projects teach you how to integrate all the concepts. Start small and gradually add complexity! 🚀
