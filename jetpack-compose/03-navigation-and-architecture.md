# Navigation and Architecture in Jetpack Compose

Goal: Master navigation patterns and architectural principles for Compose applications.

---

## 1. Navigation Basics

### What it does
Handle navigation between screens and pass data between destinations.

### Why it matters
- Multi-screen applications
- User flow management
- Data passing between screens
- Back stack management

### Intuition
Navigation is like a GPS for your app - it guides users between different screens and remembers where they've been. Each screen is a destination, and navigation connects them like roads.

### When to use
- Apps with multiple screens
- Complex user flows
- Deep linking needs
- Tab-based navigation

### Navigation Components
- **NavController**: Central navigation controller
- **NavHost**: Container for navigation destinations
- **Composable destinations**: Screen definitions
- **Arguments**: Data passing between screens

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.*

// Data classes for navigation
data class User(
    val id: String,
    val name: String,
    val email: String
)

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val description: String
)

// Navigation setup
@Composable
fun NavigationExample() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        
        composable("profile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(
                userId = userId,
                navController = navController
            )
        }
        
        composable("product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductScreen(
                productId = productId,
                navController = navController
            )
        }
        
        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}

// Home screen
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Home Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { 
                navController.navigate("profile/user123")
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Go to Profile")
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { 
                navController.navigate("product/prod123")
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Product"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("View Product")
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { 
                navController.navigate("settings")
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Settings")
            }
        }
    }
}

// Profile screen
@Composable
fun ProfileScreen(userId: String, navController: NavController) {
    // Simulate user data
    val user = remember(userId) {
        User(
            id = userId,
            name = "John Doe",
            email = "john.doe@example.com"
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar with back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // User info
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "User ID: ${user.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Actions
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { 
                navController.navigate("product/prod456")
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Products"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("View My Products")
            }
        }
    }
}

// Product screen
@Composable
fun ProductScreen(productId: String, navController: NavController) {
    // Simulate product data
    val product = remember(productId) {
        Product(
            id = productId,
            name = "Awesome Product",
            price = 29.99,
            description = "This is an amazing product that you'll love!"
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Product Details",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Product info
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Product ID: ${product.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { 
                    navController.navigate("profile/user123")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("View Profile")
            }
            
            Button(
                onClick = { 
                    navController.navigate("settings")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Settings")
            }
        }
    }
}

// Settings screen
@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Settings options
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { 
                navController.navigate("profile/user123")
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Profile Settings")
                    Text(
                        text = "Update your profile information",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { 
                navController.navigate("product/prod789")
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Products"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Product Preferences")
                    Text(
                        text = "Manage your product recommendations",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate"
                )
            }
        }
    }
}
```

---

## 2. Bottom Navigation

### What it does
Create tab-based navigation with bottom navigation bar.

### Why it matters
- Common navigation pattern
- Easy access to main sections
- Visual navigation indicators
- Single activity architecture

### Intuition
Bottom navigation is like a remote control for your app - quick access buttons to jump between main sections without going through menus.

### When to use
- Apps with 3-5 main sections
- Frequent switching between screens
- Primary navigation pattern
- Mobile-first applications

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.*

// Bottom navigation data class
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

// Bottom navigation items
val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, "home"),
    BottomNavItem("Search", Icons.Default.Search, "search"),
    BottomNavItem("Profile", Icons.Default.Person, "profile"),
    BottomNavItem("Settings", Icons.Default.Settings, "settings")
)

// Main app with bottom navigation
@Composable
fun MainAppWithBottomNav() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeContent(navController = navController)
            }
            composable("search") {
                SearchContent(navController = navController)
            }
            composable("profile") {
                ProfileContent(navController = navController)
            }
            composable("settings") {
                SettingsContent(navController = navController)
            }
            composable("detail/{itemId}") { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                DetailScreen(
                    itemId = itemId,
                    navController = navController
                )
            }
        }
    }
}

// Bottom navigation bar
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to avoid
                        // building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

// Content screens
@Composable
fun HomeContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineMedium
        )
        
        // Sample items
        val items = listOf("Item 1", "Item 2", "Item 3", "Item 4")
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.size) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("detail/item${index + 1}")
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                                text = "${index + 1}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Text(
                            text = items[index],
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Navigate"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchContent(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    val items = listOf("Apple", "Banana", "Orange", "Grape", "Mango")
    
    val filteredItems = remember(searchText) {
        if (searchText.isBlank()) {
            items
        } else {
            items.filter { it.contains(searchText, ignoreCase = true) }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Search",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search items") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredItems.size) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("detail/${filteredItems[index]}")
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Text(
                            text = filteredItems[index],
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Text(
                    text = "john.doe@example.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        navController.navigate("detail/profile_settings")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Profile")
                }
            }
        }
    }
}

@Composable
fun SettingsContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )
        
        val settingsItems = listOf(
            "Account Settings" to Icons.Default.Person,
            "Notifications" to Icons.Default.Notifications,
            "Privacy" to Icons.Default.Security,
            "Help" to Icons.Default.Help
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(settingsItems.size) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("detail/${settingsItems[index].first}")
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = settingsItems[index].second,
                            contentDescription = null
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Text(
                            text = settingsItems[index].first,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Navigate"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(itemId: String, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Detail",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Item: $itemId",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "This is a detailed view of $itemId. You can add more information here about the selected item.",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Go Back")
                }
            }
        }
    }
}
```

---

## 3. MVVM Architecture

### What it does
Separate UI logic from business logic using Model-View-ViewModel pattern.

### Why it matters
- Testable code
- Separation of concerns
- Reusable business logic
- Maintainable architecture

### Intuition
MVVM is like a restaurant - the View is the dining area (what customers see), the ViewModel is the waiter (takes orders and coordinates), and the Model is the kitchen (prepares the data). Each has a clear role.

### When to use
- Complex applications
- Team development
- Long-term maintenance
- Testing requirements

### Architecture Components
- **View**: Composable UI
- **ViewModel**: Business logic and state
- **Repository**: Data access layer
- **Model**: Data entities

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Data models
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filter: TaskFilter = TaskFilter.ALL
)

enum class TaskFilter {
    ALL, ACTIVE, COMPLETED
}

// Repository for data access
interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: String)
}

class MockTaskRepository : TaskRepository {
    private val tasks = mutableListOf(
        Task("1", "Buy groceries", "Get milk, eggs, and bread", false),
        Task("2", "Call mom", "Remember to call mom this weekend", false),
        Task("3", "Finish project", "Complete the Android project by Friday", true),
        Task("4", "Exercise", "Go for a 30-minute run", false),
        Task("5", "Read book", "Read at least one chapter", true)
    )
    
    override suspend fun getTasks(): List<Task> {
        kotlinx.coroutines.delay(500) // Simulate network delay
        return tasks.toList()
    }
    
    override suspend fun addTask(task: Task) {
        kotlinx.coroutines.delay(300) // Simulate network delay
        tasks.add(task)
    }
    
    override suspend fun updateTask(task: Task) {
        kotlinx.coroutines.delay(300) // Simulate network delay
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            tasks[index] = task
        }
    }
    
    override suspend fun deleteTask(taskId: String) {
        kotlinx.coroutines.delay(300) // Simulate network delay
        tasks.removeAll { it.id == taskId }
    }
}

// ViewModel
class TaskViewModel(
    private val repository: TaskRepository = MockTaskRepository()
) : ViewModel() {
    
    private val _state = MutableStateFlow(TaskListState())
    val state: StateFlow<TaskListState> = _state.asStateFlow()
    
    init {
        loadTasks()
    }
    
    fun loadTasks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val tasks = repository.getTasks()
                _state.value = _state.value.copy(
                    tasks = tasks,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            val task = Task(
                id = java.util.UUID.randomUUID().toString(),
                title = title,
                description = description,
                isCompleted = false
            )
            
            try {
                repository.addTask(task)
                loadTasks() // Refresh the list
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
    
    fun toggleTask(taskId: String) {
        viewModelScope.launch {
            val currentState = _state.value
            val task = currentState.tasks.find { it.id == taskId }
            
            if (task != null) {
                try {
                    val updatedTask = task.copy(isCompleted = !task.isCompleted)
                    repository.updateTask(updatedTask)
                    loadTasks() // Refresh the list
                } catch (e: Exception) {
                    _state.value = _state.value.copy(error = e.message)
                }
            }
        }
    }
    
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                repository.deleteTask(taskId)
                loadTasks() // Refresh the list
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
    
    fun setFilter(filter: TaskFilter) {
        _state.value = _state.value.copy(filter = filter)
    }
    
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}

// Main task screen (View)
@Composable
fun TaskScreen(viewModel: TaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val state by viewModel.state.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tasks",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Button(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add task",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filter chips
        val filters = TaskFilter.values()
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                FilterChip(
                    selected = state.filter == filter,
                    onClick = { viewModel.setFilter(filter) },
                    label = { Text(filter.name) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Content
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            state.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { 
                            viewModel.clearError()
                            viewModel.loadTasks()
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
            
            else -> {
                val filteredTasks = when (state.filter) {
                    TaskFilter.ALL -> state.tasks
                    TaskFilter.ACTIVE -> state.tasks.filter { !it.isCompleted }
                    TaskFilter.COMPLETED -> state.tasks.filter { it.isCompleted }
                }
                
                if (filteredTasks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tasks found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredTasks) { task ->
                            TaskItem(
                                task = task,
                                onToggle = { viewModel.toggleTask(task.id) },
                                onDelete = { viewModel.deleteTask(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Add task dialog
    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onAddTask = { title, description ->
                viewModel.addTask(title, description)
                showAddDialog = false
            }
        )
    }
}

// Task item component
@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
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
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = if (task.isCompleted) {
                        MaterialTheme.typography.titleMedium.copy(
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                        )
                    } else {
                        MaterialTheme.typography.titleMedium
                    }
                )
                
                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = if (task.isCompleted) {
                            MaterialTheme.typography.bodySmall.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        } else {
                            MaterialTheme.typography.bodySmall
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete task"
                )
            }
        }
    }
}

// Add task dialog
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (title: String, description: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddTask(title, description)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
```

---

## Practice Projects

### Easy
1. **Simple Navigation App** - Basic screen navigation
2. **Bottom Navigation Demo** - Tab-based navigation
3. **Single Screen MVVM** - Basic ViewModel pattern

### Medium
1. **Multi-Screen App** - Complex navigation flows
2. **Task Manager MVVM** - Complete MVVM architecture
3. **Settings Screen** - Navigation with parameters

### Hard
1. **E-commerce App** - Complex navigation and architecture
2. **Social Media App** - Bottom navigation with deep linking
3. **Productivity Suite** - Multiple navigation patterns

---

**Remember**: Good architecture makes your app scalable and maintainable. Think about separation of concerns from the beginning! 🚀
