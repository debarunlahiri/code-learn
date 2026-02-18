# State Management and Side Effects in Jetpack Compose

Goal: Master advanced state management and side effect handling in Compose.

---

## 1. Advanced State Management

### What it does
Handle complex state scenarios and data flow in Compose applications.

### Why it matters
- Scalable state architecture
- Performance optimization
- Predictable data flow
- Bug prevention

### Intuition
State management is like plumbing for your app - it controls how data flows between components. Good state management ensures data flows smoothly and predictably, like water through well-designed pipes.

### When to use
- Complex applications
- Shared state between screens
- Performance-critical UI
- Data persistence needs

### State Patterns
- **State Hoisting**: Lift state up to common ancestor
- **ViewModel**: Survive configuration changes
- **StateHolder**: Encapsulate complex logic
- **Redux-like**: Single source of truth

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

// ViewModel for state management
class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun increment() {
        _count.value += 1
    }
    
    fun decrement() {
        _count.value -= 1
    }
    
    fun reset() {
        _count.value = 0
    }
    
    fun simulateAsyncOperation() {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(2000) // Simulate network call
            _count.value += 10
            _isLoading.value = false
        }
    }
}

// Using ViewModel in Compose
@Composable
fun CounterScreen(viewModel: CounterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val count by viewModel.count.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { viewModel.decrement() }) {
                Text("-")
            }
            Button(onClick = { viewModel.increment() }) {
                Text("+")
            }
            Button(onClick = { viewModel.reset() }) {
                Text("Reset")
            }
        }
        
        Button(
            onClick = { viewModel.simulateAsyncOperation() },
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Add 10 (Async)")
            }
        }
    }
}

// State holder pattern for complex UI state
data class TodoItem(
    val id: String,
    val title: String,
    val isCompleted: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

data class TodoListState(
    val todos: List<TodoItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filter: TodoFilter = TodoFilter.ALL
)

enum class TodoFilter {
    ALL, ACTIVE, COMPLETED
}

class TodoListStateHolder {
    private val _state = MutableStateFlow(TodoListState())
    val state: StateFlow<TodoListState> = _state.asStateFlow()
    
    fun addTodo(title: String) {
        val currentState = _state.value
        val newTodo = TodoItem(
            id = java.util.UUID.randomUUID().toString(),
            title = title,
            isCompleted = false
        )
        _state.value = currentState.copy(
            todos = currentState.todos + newTodo
        )
    }
    
    fun toggleTodo(id: String) {
        val currentState = _state.value
        _state.value = currentState.copy(
            todos = currentState.todos.map { todo ->
                if (todo.id == id) {
                    todo.copy(isCompleted = !todo.isCompleted)
                } else {
                    todo
                }
            }
        )
    }
    
    fun deleteTodo(id: String) {
        val currentState = _state.value
        _state.value = currentState.copy(
            todos = currentState.todos.filter { it.id != id }
        )
    }
    
    fun setFilter(filter: TodoFilter) {
        val currentState = _state.value
        _state.value = currentState.copy(filter = filter)
    }
    
    fun clearCompleted() {
        val currentState = _state.value
        _state.value = currentState.copy(
            todos = currentState.todos.filter { !it.isCompleted }
        )
    }
}

// Todo list screen using state holder
@Composable
fun TodoListScreen() {
    val stateHolder = remember { TodoListStateHolder() }
    val state by stateHolder.state.collectAsState()
    
    var newTodoText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Add todo input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                label = { Text("New todo") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newTodoText.isNotBlank()) {
                        stateHolder.addTodo(newTodoText)
                        newTodoText = ""
                    }
                }
            ) {
                Text("Add")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filter chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TodoFilter.values().forEach { filter ->
                FilterChip(
                    selected = state.filter == filter,
                    onClick = { stateHolder.setFilter(filter) },
                    label = { Text(filter.name) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Todo list
        val filteredTodos = when (state.filter) {
            TodoFilter.ALL -> state.todos
            TodoFilter.ACTIVE -> state.todos.filter { !it.isCompleted }
            TodoFilter.COMPLETED -> state.todos.filter { it.isCompleted }
        }
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredTodos) { todo ->
                TodoItemRow(
                    todo = todo,
                    onToggle = { stateHolder.toggleTodo(todo.id) },
                    onDelete = { stateHolder.deleteTodo(todo.id) }
                )
            }
        }
        
        if (state.todos.any { it.isCompleted }) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { stateHolder.clearCompleted() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Completed")
            }
        }
    }
}

@Composable
fun TodoItemRow(
    todo: TodoItem,
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
                checked = todo.isCompleted,
                onCheckedChange = { onToggle() }
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = todo.title,
                modifier = Modifier.weight(1f),
                style = if (todo.isCompleted) {
                    MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                    )
                } else {
                    MaterialTheme.typography.bodyMedium
                }
            )
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete todo"
                )
            }
        }
    }
}
```

---

## 2. Side Effects

### What it does
Handle operations that should happen in response to state changes or composition events.

### Why it matters
- Proper lifecycle management
- Resource cleanup
- API calls and database operations
- Animation and navigation

### Intuition
Side effects are like actions that happen "on the side" of your UI rendering. They're the things your app needs to do besides just drawing UI - like fetching data, setting up listeners, or navigating to other screens.

### When to use
- API calls and network requests
- Database operations
- Setting up and cleaning up resources
- Navigation triggers
- Analytics logging

### Side Effect Handlers
- **LaunchedEffect**: Run suspend functions
- **SideEffect**: Run non-suspend functions
- **rememberCoroutineScope**: Get coroutine scope
- **DisposableEffect**: Cleanup on key changes
- **produceState**: Convert external data to Compose state

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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// LaunchedEffect for one-time operations
@Composable
fun OneTimeOperationExample() {
    var data by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    // This will run when the composable first enters composition
    LaunchedEffect(Unit) {
        isLoading = true
        delay(2000) // Simulate network call
        data = "Data loaded successfully!"
        isLoading = false
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading data...")
        } else {
            data?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// LaunchedEffect with dependencies
@Composable
fun DependentOperationExample(userId: String) {
    var userData by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    // This will run whenever userId changes
    LaunchedEffect(userId) {
        isLoading = true
        delay(1000) // Simulate API call
        userData = "User data for $userId"
        isLoading = false
    }
    
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            userData?.let {
                Text(it)
            }
        }
    }
}

// SideEffect for non-suspend operations
@Composable
fun AnalyticsExample(screenName: String) {
    // This runs on every recomposition
    SideEffect {
        // Log screen view to analytics
        println("Screen viewed: $screenName")
    }
    
    Text("Current screen: $screenName")
}

// rememberCoroutineScope for user-initiated actions
@Composable
fun CoroutineScopeExample() {
    var message by remember { mutableStateOf("Click the button!") }
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                coroutineScope.launch {
                    message = "Loading..."
                    delay(2000)
                    message = "Operation completed!"
                }
            }
        ) {
            Text("Start Operation")
        }
    }
}

// DisposableEffect for resource management
@Composable
fun ResourceManagementExample() {
    var isListening by remember { mutableStateOf(false) }
    
    DisposableEffect(isListening) {
        val listener = object {
            fun start() {
                println("Listener started")
            }
            
            fun stop() {
                println("Listener stopped")
            }
        }
        
        if (isListening) {
            listener.start()
        }
        
        // Cleanup when effect leaves composition or key changes
        onDispose {
            if (isListening) {
                listener.stop()
            }
        }
    }
    
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Listener status: ${if (isListening) "Active" else "Inactive"}")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { isListening = !isListening }
        ) {
            Text(if (isListening) "Stop" else "Start")
        }
    }
}

// produceState for converting external data to Compose state
@Composable
fun <T, R> produceStateExample(
    externalSource: T,
    transform: suspend (T) -> R
): State<R?> {
    return produceState<R?>(null, externalSource) {
        value = transform(externalSource)
    }
}

// Real-world example: API data fetching
class UserRepository {
    suspend fun getUser(id: String): User {
        delay(1500) // Simulate network delay
        return User(id = id, name = "User $id", email = "user$id@example.com")
    }
    
    suspend fun getUsers(): List<User> {
        delay(1000) // Simulate network delay
        return (1..10).map { 
            User(id = it.toString(), name = "User $it", email = "user$it@example.com")
        }
    }
}

data class User(
    val id: String,
    val name: String,
    val email: String
)

@Composable
fun UserScreen(userId: String) {
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(userId) {
        isLoading = true
        error = null
        try {
            val repository = UserRepository()
            user = repository.getUser(userId)
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }
    
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $error")
            }
        }
        user != null -> {
            UserProfile(user = user!!)
        }
    }
}

@Composable
fun UserProfile(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                text = "ID: ${user.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Lifecycle-aware state collection
@Composable
fun LifecycleAwareExample() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val repository = remember { UserRepository() }
    
    // This automatically pauses/resumes based on lifecycle
    val users by produceState<List<User>?>(null, lifecycleOwner.lifecycle.currentState) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            value = repository.getUsers()
        }
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        users?.let { userList ->
            items(userList) { user ->
                UserListItem(user = user)
            }
        }
    }
}

@Composable
fun UserListItem(user: User) {
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

## 3. State Optimization

### What it does
Optimize state management for better performance and fewer recompositions.

### Why it matters
- Smooth animations
- Better battery life
- Responsive UI
- Scalable applications

### Intuition
State optimization is like organizing your workshop - keep frequently used tools handy and rarely used ones stored away. This prevents unnecessary work and keeps everything running smoothly.

### When to use
- Performance-critical applications
- Complex UI with many elements
- Animation-heavy interfaces
- Large datasets

### Optimization Techniques
- **derivedStateOf**: Compute state only when dependencies change
- **remember**: Cache expensive calculations
- **key**: Control recomposition in lists
- **stable types**: Prevent unnecessary recompositions

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// derivedStateOf for expensive calculations
@Composable
fun OptimizedFilterExample() {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    
    val items = remember {
        listOf(
            Item("Apple", "Fruit", 1.99),
            Item("Banana", "Fruit", 0.99),
            Item("Carrot", "Vegetable", 1.49),
            Item("Broccoli", "Vegetable", 2.99),
            Item("Chicken", "Meat", 5.99),
            Item("Beef", "Meat", 8.99)
        )
    }
    
    // Only recalculate when dependencies actually change
    val filteredItems by remember {
        derivedStateOf {
            items.filter { item ->
                val matchesSearch = item.name.contains(searchText, ignoreCase = true)
                val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
                matchesSearch && matchesCategory
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search input
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Category filter
        val categories = remember { listOf("All", "Fruit", "Vegetable", "Meat") }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Results: ${filteredItems.size}",
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Filtered list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(filteredItems) { item ->
                ItemRow(item = item)
            }
        }
    }
}

data class Item(
    val name: String,
    val category: String,
    val price: Double
)

@Composable
fun ItemRow(item: Item) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "$${item.price}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Remember for expensive calculations
@Composable
fun ExpensiveCalculationExample() {
    var input by remember { mutableStateOf(100) }
    
    // This calculation only runs when input changes
    val result by remember(input) {
        // Simulate expensive calculation
        (1..input).fold(0L) { acc, i -> acc + i * i }
    }
    
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sum of squares: $result")
        Spacer(modifier = Modifier.height(16.dp))
        
        Slider(
            value = input.toFloat(),
            onValueChange = { input = it.toInt() },
            valueRange = 10f..1000f,
            steps = 99
        )
    }
}

// Stable types for optimization
@Stable
data class UserProfile(
    val name: String,
    val email: String,
    val isActive: Boolean
)

@Composable
fun StableTypeExample() {
    var profiles by remember {
        mutableStateOf(
            listOf(
                UserProfile("Alice", "alice@example.com", true),
                UserProfile("Bob", "bob@example.com", false),
                UserProfile("Charlie", "charlie@example.com", true)
            )
        )
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(profiles) { profile ->
            // This won't recompose unnecessarily because UserProfile is stable
            ProfileCard(profile = profile)
        }
    }
}

@Composable
fun ProfileCard(profile: UserProfile) {
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
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (profile.isActive) 
                            Color.Green.copy(alpha = 0.3f) 
                        else 
                            Color.Gray.copy(alpha = 0.3f),
                        androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.name.first().toString(),
                    style = MaterialTheme.typography.titleSmall
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = profile.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = if (profile.isActive) 
                    Icons.Default.CheckCircle else Icons.Default.Circle,
                contentDescription = if (profile.isActive) "Active" else "Inactive",
                tint = if (profile.isActive) Color.Green else Color.Gray
            )
        }
    }
}

// LazyColumn optimization with keys
@Composable
fun OptimizedListExample() {
    var items by remember {
        mutableStateOf(
            (1..100).map { 
                ListItem(id = it, title = "Item $it", description = "Description for item $it")
            }
        )
    }
    
    val listState = rememberLazyListState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                // Shuffle items to test key optimization
                items = items.shuffled()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Shuffle Items")
        }
        
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Use key to help Compose track items efficiently
            items(
                items = items,
                key = { it.id }
            ) { item ->
                ListItemRow(item = item)
            }
        }
    }
}

data class ListItem(
    val id: Int,
    val title: String,
    val description: String
)

@Composable
fun ListItemRow(item: ListItem) {
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
```

---

## Practice Projects

### Easy
1. **Simple Counter with ViewModel** - Basic state management
2. **Form Validation** - State hoisting and validation
3. **Loading States** - Side effects and loading indicators

### Medium
1. **Todo App with ViewModel** - Complex state management
2. **User Profile Screen** - API calls and side effects
3. **Search and Filter** - derivedStateOf optimization

### Hard
1. **Chat Application** - Complex state and real-time updates
2. **E-commerce Cart** - Multiple state sources and optimization
3. **Music Player** - Complex state with media controls

---

**Remember**: Good state management is the foundation of scalable Compose applications. Think about data flow and performance from the start! 🚀
