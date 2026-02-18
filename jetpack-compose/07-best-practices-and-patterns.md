# Best Practices and Patterns in Jetpack Compose

Goal: Master industry best practices and common patterns for production-ready Compose applications.

---

## 1. Architecture Patterns

### What it does
Provide structured approaches for organizing Compose applications.

### Why it matters
- Scalable codebase
- Maintainable architecture
- Team collaboration
- Code reusability

### Intuition
Architecture patterns are like blueprints for building a house - they provide a proven structure that ensures stability and functionality.

### When to use
- New projects
- Large applications
- Team development
- Long-term maintenance

### Common Patterns
- **MVVM**: Model-View-ViewModel
- **MVI**: Model-View-Intent
- **Clean Architecture**: Layered approach
- **Repository Pattern**: Data access abstraction

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Clean Architecture Example

// Domain Layer
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatar: String? = null
)

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUserById(id: String): Result<User>
    suspend fun updateUser(user: User): Result<User>
}

class GetUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return repository.getUsers()
    }
}

class GetUserByIdUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: String): Result<User> {
        return repository.getUserById(id)
    }
}

// Data Layer
class UserRepositoryImpl(
    private val api: UserApi,
    private val cache: UserCache
) : UserRepository {
    
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            // Try cache first
            val cachedUsers = cache.getUsers()
            if (cachedUsers.isNotEmpty()) {
                Result.success(cachedUsers)
            } else {
                // Fetch from API
                val users = api.getUsers()
                cache.saveUsers(users)
                Result.success(users)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserById(id: String): Result<User> {
        return try {
            val cachedUser = cache.getUserById(id)
            if (cachedUser != null) {
                Result.success(cachedUser)
            } else {
                val user = api.getUserById(id)
                cache.saveUser(user)
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateUser(user: User): Result<User> {
        return try {
            val updatedUser = api.updateUser(user)
            cache.saveUser(updatedUser)
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Presentation Layer - ViewModel
sealed class UserListUiState {
    object Loading : UserListUiState()
    data class Success(val users: List<User>) : UserListUiState()
    data class Error(val message: String) : UserListUiState()
}

class UserListViewModel(
    private val getUsersUseCase: GetUserUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()
    
    init {
        loadUsers()
    }
    
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UserListUiState.Loading
            
            when (val result = getUsersUseCase()) {
                is Result.Success -> {
                    _uiState.value = UserListUiState.Success(result.data)
                }
                is Result.Failure -> {
                    _uiState.value = UserListUiState.Error(
                        result.exception?.message ?: "Unknown error"
                    )
                }
            }
        }
    }
    
    fun refresh() {
        loadUsers()
    }
}

// UI Layer - Composable
@Composable
fun UserListScreen(
    viewModel: UserListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (val state = uiState) {
        is UserListUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        is UserListUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.users) { user ->
                    UserItem(user = user)
                }
            }
        }
        
        is UserListUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.refresh() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
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
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // User info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = { /* Handle click */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
        }
    }
}
```

---

## 2. Component Design Patterns

### What it does
Provide guidelines for creating reusable and maintainable UI components.

### Why it matters
- Code reusability
- Consistent design
- Easier testing
- Better maintainability

### Intuition
Component design patterns are like LEGO building instructions - they show you how to create reusable pieces that can be combined in many ways.

### When to use
- Custom component creation
- Design system development
- Component libraries
- Large applications

### Design Patterns
- **Compound Components**: Related components together
- **Slot-based APIs**: Flexible content placement
- **State Hoisting**: Lift state up
- **Composition over Inheritance**: Build complex from simple

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// Compound Component Pattern
@Composable
fun SearchableList(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    
    val filteredItems = remember(searchText, items) {
        if (searchText.isBlank()) {
            items
        } else {
            items.filter { it.contains(searchText, ignoreCase = true) }
        }
    }
    
    Column(modifier = modifier) {
        // Search bar (part of compound component)
        SearchBar(
            query = searchText,
            onQueryChange = { searchText = it },
            onSearch = { isSearching = false },
            isActive = isSearching,
            onActiveChange = { isSearching = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Search suggestions/results
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredItems) { item ->
                    SearchSuggestion(
                        suggestion = item,
                        onSuggestionClick = {
                            onItemSelected(item)
                            searchText = item
                            isSearching = false
                        }
                    )
                }
            }
        }
        
        // Main content
        if (!isSearching) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems) { item ->
                    ListItem(
                        headlineContent = { Text(item) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.clickable { onItemSelected(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchSuggestion(
    suggestion: String,
    onSuggestionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSuggestionClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(suggestion)
    }
}

// Slot-based API Pattern
@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    header: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
    actions: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column {
            // Header slot
            header?.invoke()
            
            // Content slot
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                content()
            }
            
            // Actions slot
            actions?.invoke()
        }
    }
}

// Usage examples of slot-based API
@Composable
fun UserProfileCard(user: User) {
    CustomCard(
        header = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "User Profile",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { /* Handle edit */ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }
        },
        content = {
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Handle message */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Message")
                }
                Button(
                    onClick = { /* Handle follow */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Follow")
                }
            }
        }
    )
}

@Composable
fun SettingsCard() {
    CustomCard(
        header = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        content = {
            Column {
                SettingsItem(title = "Notifications", subtitle = "Manage your notification preferences")
                SettingsItem(title = "Privacy", subtitle = "Control your privacy settings")
                SettingsItem(title = "Account", subtitle = "Manage your account information")
            }
        }
    )
}

@Composable
fun SettingsItem(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// State Hoisting Pattern
@Composable
fun CounterWithStateHoisting(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Count: $count",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onDecrement) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrement"
                    )
                }
                
                Button(onClick = onIncrement) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increment"
                    )
                }
                
                Button(onClick = onReset) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset"
                    )
                }
            }
        }
    }
}

// Parent component that manages state
@Composable
fun CounterScreen() {
    var count by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "State Hoisting Example",
            style = MaterialTheme.typography.headlineMedium
        )
        
        CounterWithStateHoisting(
            count = count,
            onIncrement = { count++ },
            onDecrement = { count-- },
            onReset = { count = 0 }
        )
        
        // Multiple counters sharing the same state
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CounterWithStateHoisting(
                count = count,
                onIncrement = { count++ },
                onDecrement = { count-- },
                onReset = { count = 0 },
                modifier = Modifier.weight(1f)
            )
            
            CounterWithStateHoisting(
                count = count,
                onIncrement = { count++ },
                onDecrement = { count-- },
                onReset = { count = 0 },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
```

---

## 3. Performance Best Practices

### What it does
Provide guidelines for optimizing Compose application performance.

### Why it matters
- Smooth user experience
- Better battery life
- Responsive UI
- Scalable applications

### Intuition
Performance optimization is like tuning a car engine - make small adjustments to get the best performance without breaking anything.

### When to use
- All applications
- Performance-critical features
- Large datasets
- Complex animations

### Performance Practices
- **Minimize recompositions**: Smart state management
- **Use lazy layouts**: Efficient scrolling
- **Optimize images**: Proper loading and caching
- **Profile regularly**: Identify bottlenecks

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

// Performance-optimized list with stable types
@Stable
data class OptimizedItem(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: Long,
    val isFavorite: Boolean
)

@Composable
fun OptimizedListScreen() {
    var items by remember {
        mutableStateOf(
            (1..1000).map { index ->
                OptimizedItem(
                    id = index.toString(),
                    title = "Item $index",
                    description = "Description for item $index with some longer text",
                    timestamp = System.currentTimeMillis() - index * 1000,
                    isFavorite = index % 5 == 0
                )
            }
        )
    }
    
    val listState = rememberLazyListState()
    var searchText by remember { mutableStateOf("") }
    
    // Derived state for filtering
    val filteredItems by remember(searchText, items) {
        if (searchText.isBlank()) {
            items
        } else {
            items.filter { 
                it.title.contains(searchText, ignoreCase = true) ||
                it.description.contains(searchText, ignoreCase = true)
            }
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        
        // Optimized list
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = filteredItems,
                key = { it.id }
            ) { item ->
                OptimizedListItem(
                    item = item,
                    onFavoriteToggle = { id ->
                        items = items.map {
                            if (it.id == id) it.copy(isFavorite = !it.isFavorite)
                            else it
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun OptimizedListItem(
    item: OptimizedItem,
    onFavoriteToggle: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onFavoriteToggle(item.id) }
                    ) {
                        Icon(
                            imageVector = if (item.isFavorite) 
                                Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (item.isFavorite) "Unfavorite" else "Favorite",
                            tint = if (item.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    IconButton(
                        onClick = { isExpanded = !isExpanded }
                    ) {
                        Icon(
                            imageVector = if (isExpanded) 
                                Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand"
                        )
                    }
                }
            }
        }
    }
}

// Image loading optimization
@Composable
fun OptimizedImageCard(
    imageUrl: String,
    title: String,
    description: String
) {
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (hasError) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.BrokenImage,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "Failed to load image",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                
                // In real app, use Coil or Glide with proper caching
                // AsyncImage(
                //     model = imageUrl,
                //     contentDescription = title,
                //     modifier = Modifier.fillMaxSize(),
                //     contentScale = ContentScale.Crop,
                //     onLoading = { isLoading = true },
                //     onSuccess = { isLoading = false },
                //     onError = { hasError = true }
                // )
            }
            
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    
    // Simulate image loading
    LaunchedEffect(imageUrl) {
        isLoading = true
        hasError = false
        delay(1000 + (imageUrl.hashCode() % 2000)) // Variable load times
        isLoading = false
        if (imageUrl.contains("error")) { // Simulate errors
            hasError = true
        }
    }
}
```

---

## 4. Code Organization

### What does
Provide guidelines for organizing Compose code effectively.

### Why it matters
- Maintainable codebase
- Easy navigation
- Team collaboration
- Code reusability

### Intuition
Code organization is like organizing a library - put related books together, label sections clearly, and make everything easy to find.

### When to use
- All projects
- Large codebases
- Team development
- Long-term maintenance

### Organization Practices
- **Feature-based structure**: Group by feature
- **Shared components**: Common UI elements
- **Single responsibility**: One purpose per file
- **Clear naming**: Descriptive names

### Java/Kotlin Code
```kotlin
// File structure example:
// com.example.app/
// ├── ui/
// │   ├── components/
// │   │   ├── buttons/
// │   │   │   ├── PrimaryButton.kt
// │   │   │   └── SecondaryButton.kt
// │   │   ├── cards/
// │   │   │   ├── UserCard.kt
// │   │   │   └── ProductCard.kt
// │   │   └── forms/
// │   │       ├── LoginForm.kt
// │   │       └── RegistrationForm.kt
// │   ├── theme/
// │   │   ├── Color.kt
// │   │   ├── Theme.kt
// │   │   └── Type.kt
// │   └── screens/
// │       ├── home/
// │       │   ├── HomeScreen.kt
// │       │   └── HomeViewModel.kt
// │       └── profile/
// │           ├── ProfileScreen.kt
// │           └── ProfileViewModel.kt
// ├── data/
// │   ├── repository/
// │   ├── models/
// │   └── network/
// └── domain/
//     ├── usecases/
//     └── models/

// Shared UI Components
object AppComponents {
    
    @Composable
    fun PrimaryButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true
    ) {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled
        ) {
            Text(text)
        }
    }
    
    @Composable
    fun SecondaryButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled
        ) {
            Text(text)
        }
    }
    
    @Composable
    fun LoadingIndicator(
        modifier: Modifier = Modifier,
        message: String = "Loading..."
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    
    @Composable
    fun ErrorView(
        message: String,
        onRetry: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}

// Screen template
@Composable
fun ScreenTemplate(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    loading: Boolean = false,
    error: String? = null,
    onRetry: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = onBackClick?.let { {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                } },
                actions = actions
            )
        }
    ) { paddingValues ->
        when {
            loading -> {
                AppComponents.LoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            error != null -> {
                AppComponents.ErrorView(
                    message = error,
                    onRetry = onRetry ?: {},
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                content(paddingValues)
            }
        }
    }
}

// Usage example
@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    ScreenTemplate(
        title = "Example Screen",
        loading = uiState.isLoading,
        error = uiState.error,
        onRetry = { viewModel.loadData() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.items) { item ->
                Text(
                    text = item,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
```

---

## Practice Projects

### Easy
1. **Component Library** - Create reusable components
2. **State Management** - Implement proper state hoisting
3. **Code Organization** - Structure a small app

### Medium
1. **Clean Architecture App** - Implement layered architecture
2. **Performance Optimization** - Optimize an existing app
3. **Design System** - Create consistent UI components

### Hard
1. **Large Scale App** - Architecture for complex application
2. **Component Testing** - Test component patterns
3. **Performance Profiling** - Advanced optimization

---

**Remember**: Good practices and patterns are the foundation of maintainable, scalable applications. Invest time in learning and applying them! 🚀
