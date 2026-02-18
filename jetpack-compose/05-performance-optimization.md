# Performance Optimization in Jetpack Compose

Goal: Master performance optimization techniques for smooth, efficient Compose applications.

---

## 1. Recomposition Optimization

### What it does
Minimize unnecessary recompositions and improve rendering performance.

### Why it matters
- Smooth 60fps animations
- Better battery life
- Responsive UI
- Scalable applications

### Intuition
Recomposition is like repainting a room - you only want to repaint the walls that changed, not the entire house every time. Smart recomposition means touching up only what needs updating.

### When to use
- Complex UI with many elements
- Animation-heavy interfaces
- Large datasets
- Performance-critical applications

### Optimization Techniques
- **remember**: Cache expensive calculations
- **derivedStateOf**: Compute state only when needed
- **Stable types**: Prevent unnecessary recompositions
- **Key parameters**: Control list recompositions

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Stable data class for optimization
@Stable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val isActive: Boolean
)

// Expensive calculation optimization
@Composable
fun ExpensiveCalculationExample() {
    var input by remember { mutableStateOf(100) }
    
    // This calculation only runs when input changes
    val result by remember(input) {
        // Simulate expensive calculation
        (1..input).fold(0L) { acc, i -> acc + i * i }
    }
    
    // Derived state that only recalculates when dependencies change
    val isLargeNumber by remember {
        derivedStateOf { result > 10000 }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Sum of squares: $result",
            style = MaterialTheme.typography.titleLarge
        )
        
        Text(
            text = if (isLargeNumber) "Large number!" else "Small number",
            color = if (isLargeNumber) Color.Red else Color.Green,
            fontWeight = FontWeight.Bold
        )
        
        Slider(
            value = input.toFloat(),
            onValueChange = { input = it.toInt() },
            valueRange = 10f..1000f,
            steps = 99
        )
    }
}

// Optimized list with keys
@Composable
fun OptimizedListExample() {
    var users by remember {
        mutableStateOf(
            listOf(
                User("1", "Alice", "alice@example.com", true),
                User("2", "Bob", "bob@example.com", false),
                User("3", "Charlie", "charlie@example.com", true),
                User("4", "Diana", "diana@example.com", false),
                User("5", "Eve", "eve@example.com", true)
            )
        )
    }
    
    val listState = rememberLazyListState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                users = users.shuffled()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Shuffle Users")
        }
        
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Use key to help Compose track items efficiently
            items(
                items = users,
                key = { it.id }
            ) { user ->
                // This won't recompose unnecessarily because User is stable
                OptimizedUserItem(user = user)
            }
        }
    }
}

@Composable
fun OptimizedUserItem(user: User) {
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
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (user.isActive) 
                            Icons.Default.CheckCircle else Icons.Default.Circle,
                        contentDescription = if (user.isActive) "Active" else "Inactive",
                        tint = if (user.isActive) Color.Green else Color.Gray
                    )
                    
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
            
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "User ID: ${user.id}\nStatus: ${if (user.isActive) "Active" else "Inactive"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Content-based recomposition control
@Composable
fun ContentBasedRecomposition() {
    var counter by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("Hello") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // This only recomposes when counter changes
        CounterDisplay(counter = counter)
        
        // This only recomposes when text changes
        TextDisplay(text = text)
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { counter++ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Increment Counter")
            }
            
            Button(
                onClick = { text = "Updated: ${System.currentTimeMillis()}" },
                modifier = Modifier.weight(1f)
            ) {
                Text("Update Text")
            }
        }
    }
}

@Composable
fun CounterDisplay(counter: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Counter",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "$counter",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TextDisplay(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Text",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
```

---

## 2. Memory Optimization

### What it does
Manage memory efficiently to prevent leaks and improve performance.

### Why it matters
- Prevent memory leaks
- Reduce GC pressure
- Better performance
- Stable applications

### Intuition
Memory optimization is like keeping your workspace clean - put away tools you're not using and don't leave things lying around. This prevents clutter and keeps everything running smoothly.

### When to use
- Long-running applications
- Image-heavy apps
- Complex data structures
- Memory-constrained devices

### Memory Techniques
- **remember**: Cache expensive objects
- **rememberSaveable**: Survive configuration changes
- **Disposable effects**: Clean up resources
- **Lazy loading**: Load data when needed

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Memory-efficient image loading
@Composable
fun MemoryEfficientImageList() {
    val images = remember {
        (1..100).map { index ->
            ImageData(
                id = index,
                url = "https://picsum.photos/seed/$index/300/200.jpg",
                title = "Image $index"
            )
        }
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = images,
            key = { it.id }
        ) { image ->
            ImageItem(image = image)
        }
    }
}

data class ImageData(
    val id: Int,
    val url: String,
    val title: String
)

@Composable
fun ImageItem(image: ImageData) {
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    
    // Use remember to cache the painter
    val painter = remember(image.url) {
        // In real app, use Coil or Glide
        // This is just a placeholder
        null
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text("Failed to load image")
                        }
                    }
                }
                
                // In real app, use AsyncImage from Coil
                // AsyncImage(
                //     model = image.url,
                //     contentDescription = image.title,
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
                    text = image.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "ID: ${image.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    
    // Simulate loading
    LaunchedEffect(image.url) {
        isLoading = true
        hasError = false
        delay(1000 + (image.id * 10)) // Simulate different load times
        isLoading = false
        if (image.id % 10 == 0) { // Simulate occasional errors
            hasError = true
        }
    }
}

// Resource management with DisposableEffect
@Composable
fun ResourceManagerExample() {
    var isListening by remember { mutableStateOf(false) }
    
    DisposableEffect(isListening) {
        val listener = object {
            fun start() {
                println("Resource started")
                // Start listening to sensor, location, etc.
            }
            
            fun stop() {
                println("Resource stopped")
                // Stop listening, release resources
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Resource Management",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            text = "Status: ${if (isListening) "Listening" else "Stopped"}",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Button(
            onClick = { isListening = !isListening }
        ) {
            Text(if (isListening) "Stop" else "Start")
        }
    }
}

// Lifecycle-aware state collection
@Composable
fun LifecycleAwareStateExample() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val dataProvider = remember { DataProvider() }
    
    // This automatically pauses/resumes based on lifecycle
    val data by dataProvider.data
        .collectAsStateWithLifecycle(
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Lifecycle-Aware Data",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            text = "Current data: $data",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Text(
            text = "Data updates only when app is in foreground",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

class DataProvider {
    private val _data = MutableStateFlow("Initial data")
    val data = _data.asStateFlow()
    
    init {
        // Simulate data updates
        kotlinx.coroutines.GlobalScope.launch {
            var counter = 1
            while (true) {
                kotlinx.coroutines.delay(2000)
                _data.value = "Updated data $counter"
                counter++
            }
        }
    }
}

// Lazy loading with remember
@Composable
fun LazyLoadingExample() {
    var showDetails by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Lazy Loading",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showDetails = !showDetails }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Click to toggle details",
                    style = MaterialTheme.typography.titleMedium
                )
                
                // Heavy content only loaded when needed
                if (showDetails) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HeavyContent()
                }
            }
        }
    }
}

@Composable
fun HeavyContent() {
    // This is only created when showDetails is true
    val heavyData by remember {
        mutableStateOf(
            (1..1000).map { "Item $it" }
        )
    }
    
    LazyColumn(
        modifier = Modifier.height(200.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(heavyData.take(20)) { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
```

---

## 3. Layout Performance

### What it does
Optimize layout measurements and drawing for better performance.

### Why it matters
- Faster rendering
- Smoother scrolling
- Better battery life
- Responsive UI

### Intuition
Layout performance is like organizing a bookshelf - if you arrange books efficiently, you can find and display them quickly. Poor layout organization means you're constantly rearranging everything.

### When to use
- Complex layouts
- Long scrolling lists
- Nested layouts
- Performance-critical screens

### Layout Techniques
- **ConstraintLayout**: Reduce nesting
- **Lazy layouts**: Efficient scrolling
- **SubcomposeLayout**: Dynamic content
- **Layout modifiers**: Custom measurements

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

// Efficient layout with minimal nesting
@Composable
fun EfficientLayoutExample() {
    var items by remember {
        mutableStateOf(
            (1..50).map { index ->
                ListItem(
                    id = index,
                    title = "Item $index",
                    description = "Description for item $index",
                    category = if (index % 3 == 0) "Important" else "Normal"
                )
            }
        )
    }
    
    val listState = rememberLazyListState()
    
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            // Single composable with minimal nesting
            EfficientListItem(item = item)
        }
    }
}

data class ListItem(
    val id: Int,
    val title: String,
    val description: String,
    val category: String
)

@Composable
fun EfficientListItem(item: ListItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (item.category == "Important") 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.surface,
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (item.category == "Important") 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (item.category == "Important") 
                    Icons.Default.Star else Icons.Default.Circle,
                contentDescription = null,
                tint = if (item.category == "Important") 
                    Color.White else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Category badge
        Surface(
            color = if (item.category == "Important") 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = item.category,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = if (item.category == "Important") 
                    Color.White else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Custom layout for performance
@Composable
fun CustomLayoutExample() {
    var items by remember {
        mutableStateOf(
            (1..20).map { "Item $it" }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Custom Flow Layout",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        FlowLayout(
            modifier = Modifier.fillMaxWidth(),
            horizontalSpacing = 8.dp,
            verticalSpacing = 8.dp
        ) {
            items.forEach { item ->
                Chip(
                    text = item,
                    onClick = { /* Handle click */ }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                items = items + "New Item ${items.size + 1}"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Item")
        }
    }
}

@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val verticalSpacingPx = verticalSpacing.roundToPx()
        
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        
        var x = 0
        var y = 0
        var rowHeight = 0
        
        val positions = mutableListOf<androidx.compose.ui.unit.IntOffset>()
        
        placeables.forEach { placeable ->
            if (x + placeable.width > constraints.maxWidth) {
                x = 0
                y += rowHeight + verticalSpacingPx
                rowHeight = 0
            }
            
            positions.add(androidx.compose.ui.unit.IntOffset(x, y))
            x += placeable.width + horizontalSpacingPx
            rowHeight = maxOf(rowHeight, placeable.height)
        }
        
        val width = constraints.maxWidth
        val height = y + rowHeight
        
        layout(width, height) {
            positions.forEach { position ->
                placeables.forEachIndexed { index, placeable ->
                    if (index < positions.size) {
                        placeable.place(positions[index])
                    }
                }
            }
        }
    }
}

@Composable
fun Chip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

// SubcomposeLayout for dynamic content
@Composable
fun SubcomposeLayoutExample() {
    var showHeader by remember { mutableStateOf(true) }
    var items by remember {
        mutableStateOf(
            (1..10).map { "Dynamic Item $it" }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Dynamic Layout",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        DynamicLayout(
            showHeader = showHeader,
            items = items,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showHeader = !showHeader }
            ) {
                Text(if (showHeader) "Hide Header" else "Show Header")
            }
            
            Button(
                onClick = { 
                    items = items + "New Item ${items.size + 1}"
                }
            ) {
                Text("Add Item")
            }
        }
    }
}

@Composable
fun DynamicLayout(
    showHeader: Boolean,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        // Measure header if needed
        val headerPlaceable = if (showHeader) {
            subcompose("header") {
                Header()
            }.first().measure(constraints)
        } else {
            null
        }
        
        // Measure items
        val itemPlaceables = subcompose("items") {
            items.forEach { item ->
                Item(text = item)
            }
        }.map { it.measure(constraints) }
        
        // Calculate layout
        val headerHeight = headerPlaceable?.height ?: 0
        val itemHeight = itemPlaceables.maxOfOrNull { it.height } ?: 0
        val totalHeight = headerHeight + itemPlaceables.size * itemHeight
        
        layout(constraints.maxWidth, totalHeight) {
            // Place header
            headerPlaceable?.place(0, 0)
            
            // Place items
            var y = headerHeight
            itemPlaceables.forEach { placeable ->
                placeable.place(0, y)
                y += itemHeight
            }
        }
    }
}

@Composable
fun Header() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = "Dynamic Header",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun Item(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
```

---

## 4. Performance Monitoring

### What does
Monitor and analyze app performance for optimization opportunities.

### Why it matters
- Identify bottlenecks
- Measure improvements
- Debug performance issues
- Optimize user experience

### Intuition
Performance monitoring is like having a fitness tracker for your app - it tells you where you're spending resources and helps you optimize for better results.

### When to use
- Development phase
- Performance testing
- Production monitoring
- Optimization efforts

### Monitoring Tools
- **Compose Inspector**: Visual debugging
- **Layout Inspector**: Layout analysis
- **Profiler**: CPU/memory analysis
- **Benchmarking**: Performance measurement

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.system.measureTimeMillis

// Performance monitoring example
@Composable
fun PerformanceMonitoringExample() {
    var renderTime by remember { mutableStateOf(0L) }
    var itemCount by remember { mutableStateOf(100) }
    
    // Monitor render performance
    LaunchedEffect(itemCount) {
        val time = measureTimeMillis {
            // Simulate expensive rendering
            kotlinx.coroutines.delay(10)
        }
        renderTime = time
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Performance metrics
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Performance Metrics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Render Time: ${renderTime}ms")
                Text("Item Count: $itemCount")
                Text(
                    text = if (renderTime < 16) "Good (60fps)" else "Needs Optimization",
                    color = if (renderTime < 16) Color.Green else Color.Red
                )
            }
        }
        
        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { itemCount = maxOf(10, itemCount - 10) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Less Items")
            }
            
            Button(
                onClick = { itemCount = minOf(1000, itemCount + 10) },
                modifier = Modifier.weight(1f)
            ) {
                Text("More Items")
            }
        }
        
        // List with performance monitoring
        PerformanceMonitoredList(itemCount = itemCount)
    }
}

@Composable
fun PerformanceMonitoredList(itemCount: Int) {
    val items = remember(itemCount) {
        (1..itemCount).map { index ->
            PerformanceItem(
                id = index,
                title = "Item $index",
                description = "Description for item $index with some longer text to simulate real content"
            )
        }
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            PerformanceListItem(item = item)
        }
    }
}

data class PerformanceItem(
    val id: Int,
    val title: String,
    val description: String
)

@Composable
fun PerformanceListItem(item: PerformanceItem) {
    // Monitor individual item performance
    val renderTime = remember {
        measureTimeMillis {
            // Simulate item rendering work
            kotlinx.coroutines.delay(1)
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Performance indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (renderTime < 2) Color.Green else Color.Yellow,
                        androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}

// Memory usage monitoring
@Composable
fun MemoryUsageMonitor() {
    var memoryUsage by remember { mutableStateOf(0L) }
    var isHighMemoryUsage by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        while (true) {
            val runtime = Runtime.getRuntime()
            val usedMemory = runtime.totalMemory() - runtime.freeMemory()
            memoryUsage = usedMemory / (1024 * 1024) // Convert to MB
            isHighMemoryUsage = memoryUsage > 100 // Consider high if > 100MB
            kotlinx.coroutines.delay(1000) // Update every second
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Memory Usage",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Current: ${memoryUsage}MB")
            Text(
                text = if (isHighMemoryUsage) "High Usage - Consider Optimization" else "Normal Usage",
                color = if (isHighMemoryUsage Color.Red else Color.Green
            )
        }
    }
}

// Recomposition tracking
@Composable
fun RecompositionTracker() {
    var recompositionCount by remember { mutableStateOf(0) }
    
    // Track recompositions
    LaunchedEffect(Unit) {
        recompositionCount++
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Recomposition Tracker",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Recompositions: $recompositionCount")
            Text(
                text = if (recompositionCount < 10) "Good" else "Consider Optimization",
                color = if (recompositionCount < 10) Color.Green else Color.Red
            )
        }
    }
}
```

---

## Practice Projects

### Easy
1. **Optimized Counter** - Basic state optimization
2. **Memory-Efficient List** - Simple list optimization
3. **Layout Performance** - Reduce nesting

### Medium
1. **Performance Dashboard** - Monitor app performance
2. **Optimized Image Gallery** - Memory and layout optimization
3. **Smooth Scrolling List** - Advanced list optimization

### Hard
1. **Performance Profiler** - Custom performance monitoring
2. **Complex Layout Optimization** - Advanced layout techniques
3. **Memory Leak Detector** - Identify and fix memory issues

---

**Remember**: Performance optimization is an ongoing process. Profile your app regularly and optimize based on real usage patterns! 🚀
