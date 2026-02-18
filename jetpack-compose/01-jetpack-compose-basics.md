# Jetpack Compose Basics

Goal: Master the fundamentals of Jetpack Compose for modern Android UI development.

---

## What is Jetpack Compose?

Jetpack Compose is Android's modern toolkit for building native UI. It simplifies and accelerates UI development on Android with less code, powerful tools, and intuitive Kotlin APIs.

### Why it matters
- **Declarative UI**: Describe what you want, not how to build it
- **Less Code**: Write 40% less code compared to traditional XML layouts
- **Live Preview**: See changes instantly without rebuilding
- **Type Safety**: Catch UI errors at compile time
- **Modern Architecture**: Built for reactive programming patterns

### Intuition
Think of Compose like writing HTML with React, but for Android. Instead of creating XML files and finding views with findViewById(), you just write functions that describe your UI. Like building with LEGO blocks - each function is a block that can be combined to create complex structures.

### When to use
- New Android applications
- Modern UI development
- Prototyping and rapid development
- Apps requiring dynamic UI updates
- Complex user interfaces

---

## 1. Composable Functions

### What it does
Functions annotated with @Composable that describe UI elements.

### Why it matters
- Foundation of Compose UI
- Reusable UI components
- Type-safe parameter passing
- Automatic state management

### Intuition
Composable functions are like LEGO instructions - they tell Android exactly how to build and arrange UI pieces. Each function can be small and focused, then combined into larger components.

### When to use
- Creating any UI element
- Building reusable components
- Structuring your app's UI
- Custom views and layouts

### Key Concepts
- **@Composable annotation**: Marks function as UI component
- **State management**: Automatic recomposition on state changes
- **Parameters**: Pass data and callbacks
- **Composition**: Build UI by calling other composables

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Basic composable function
@Composable
fun Greeting(name: String) {
    Text(text = "Hello, $name!")
}

// Composable with parameters and state
@Composable
fun CounterButton() {
    var count by remember { mutableStateOf(0) }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Count: $count")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { count++ }) {
            Text("Increment")
        }
    }
}

// Reusable card component
@Composable
fun InfoCard(
    title: String,
    description: String,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
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
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onCardClick) {
                Text("Learn More")
            }
        }
    }
}

// Preview for design-time visualization
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun CounterButtonPreview() {
    MaterialTheme {
        CounterButton()
    }
}
```

---

## 2. Layouts and Modifiers

### What it does
Arrange UI elements and apply styling/behavior.

### Why it matters
- Responsive design
- Consistent spacing and alignment
- Reusable styling
- Performance optimization

### Intuition
Layouts are like containers that organize your UI elements. Modifiers are like decorators that add properties (size, padding, background) to any element. Think of arranging furniture in a room (layouts) and then painting and positioning each piece (modifiers).

### When to use
- Arranging UI elements
- Adding spacing and margins
- Setting sizes and positions
- Adding click handlers and animations

### Key Layouts
- **Column**: Vertical arrangement
- **Row**: Horizontal arrangement
- **Box**: Stack elements on top of each other
- **LazyColumn/LazyRow**: Efficient scrolling lists
- **Surface**: Material Design surface

### Common Modifiers
- **padding()**: Add space around element
- **size()**: Set specific dimensions
- **fillMaxWidth()/fillMaxHeight()**: Take available space
- **background()**: Set background color/drawable
- **clickable()**: Add click interaction
- **clip()**: Apply shape clipping

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Column layout example
@Composable
fun VerticalLayoutExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Vertical Layout",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text("Item 1")
        Text("Item 2")
        Text("Item 3")
    }
}

// Row layout example
@Composable
fun HorizontalLayoutExample() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { /* TODO */ }) {
            Text("Button 1")
        }
        Button(onClick = { /* TODO */ }) {
            Text("Button 2")
        }
        Button(onClick = { /* TODO */ }) {
            Text("Button 3")
        }
    }
}

// Box layout with stacking
@Composable
fun StackedLayoutExample() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        // Background element
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue, RoundedCornerShape(8.dp))
        )
        
        // Foreground text
        Text(
            text = "Stacked\nContent",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// Complex layout with multiple modifiers
@Composable
fun ComplexLayoutExample() {
    var isPressed by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { isPressed = !isPressed }
            .background(
                if (isPressed) Color.Green.copy(alpha = 0.1f) 
                else Color.Transparent
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 8.dp else 4.dp
        )
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
                        Color.Blue,
                        RoundedCornerShape(24.dp)
                    )
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Interactive Card",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (isPressed) "Pressed!" else "Click me",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = if (isPressed) 
                    Icons.Default.Check else Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// LazyColumn for efficient scrolling
@Composable
fun ScrollingListExample() {
    val items = (1..100).map { "Item $it" }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
```

---

## 3. State Management

### What it does
Manage and update UI data over time.

### Why it matters
- Reactive UI updates
- Data flow control
- Performance optimization
- Predictable state changes

### Intuition
State is like the memory of your UI. When state changes, Compose automatically redraws only the parts that need updating. Think of it like a smart artist who only erases and redraws the parts of a painting that changed.

### When to use
- User input handling
- Data loading states
- Animation values
- Form validation
- Dynamic content

### State Types
- **remember**: Preserve state across recompositions
- **mutableStateOf**: Create observable state
- **rememberSaveable**: Survive configuration changes
- **derivedStateOf**: Computed state from other state

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// Basic state management
@Composable
fun BasicStateExample() {
    var count by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Count: $count")
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = { count-- }) {
                Text("-")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { count++ }) {
                Text("+")
            }
        }
    }
}

// Form with multiple states
@Composable
fun LoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    
    // Derived state
    val isFormValid by remember {
        derivedStateOf {
            email.isNotBlank() && password.length >= 6
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                showError = false // Clear error on input
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = showError && email.isBlank()
        )
        
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                showError = false
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            isError = showError && password.length < 6
        )
        
        if (showError) {
            Text(
                text = "Please enter valid email and password",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Button(
            onClick = {
                if (isFormValid) {
                    isLoading = true
                    // Simulate API call
                    // In real app, use viewModel or coroutine
                } else {
                    showError = true
                }
            },
            enabled = isFormValid && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login")
            }
        }
    }
}

// State with saved state
@Composable
fun CounterWithSaveState() {
    var count by rememberSaveable { mutableStateOf(0) }
    
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Count survives rotation: $count")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { count++ }) {
            Text("Increment")
        }
    }
}

// Complex state management
@Composable
fun TodoList() {
    data class TodoItem(
        val id: Int,
        val text: String,
        val isCompleted: Boolean
    )
    
    var todos by remember {
        mutableStateOf(
            listOf(
                TodoItem(1, "Learn Compose", false),
                TodoItem(2, "Build amazing UI", false),
                TodoItem(3, "Master state management", false)
            )
        )
    }
    
    var newTodoText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Add new todo
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
                        todos = todos + 
                            TodoItem(
                                todos.size + 1, 
                                newTodoText, 
                                false
                            )
                        newTodoText = ""
                    }
                }
            ) {
                Text("Add")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Todo list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(todos) { todo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todo.isCompleted,
                        onCheckedChange = { checked ->
                            todos = todos.map {
                                if (it.id == todo.id) it.copy(isCompleted = checked)
                                else it
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = todo.text,
                        modifier = Modifier.weight(1f),
                        style = if (todo.isCompleted) {
                            MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        } else {
                            MaterialTheme.typography.bodyMedium
                        }
                    )
                    
                    Button(
                        onClick = {
                            todos = todos.filter { it.id != todo.id }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}
```

---

## 4. Material Design Components

### What it does
Pre-built Material Design UI components.

### Why it matters
- Consistent design language
- Accessibility features
- Platform integration
- Rapid development

### Intuition
Material components are like pre-made furniture pieces - they're well-designed, functional, and follow design guidelines. You can customize them but don't have to build them from scratch.

### When to use
- Standard UI elements
- Material Design compliance
- Rapid prototyping
- Consistent user experience

### Common Components
- **Button**: Clickable action elements
- **TextField**: Text input
- **Card**: Content containers
- **Chip**: Compact input elements
- **Dialog**: Modal overlays
- **BottomSheet**: Slide-up panels

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// Button variants
@Composable
fun ButtonExamples() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Filled button
        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Primary Action")
        }
        
        // Outlined button
        OutlinedButton(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Secondary Action")
        }
        
        // Text button
        TextButton(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tertiary Action")
        }
        
        // Button with icon
        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Item")
        }
    }
}

// TextField examples
@Composable
fun TextFieldExamples() {
    var text by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var search by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Basic text field
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text") },
            placeholder = { Text("Type something...") },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Search field with leading icon
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Search") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (search.isNotBlank()) {
                    IconButton(onClick = { search = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Card examples
@Composable
fun CardExamples() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Basic card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Card Title",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "This is a basic card with some content.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Elevated card with action
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Elevated Card",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "With higher elevation",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { /* Handle action */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                }
            }
        }
        
        // Clickable card
        Card(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
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
                            RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Interactive Card",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Click to interact",
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
    }
}

// Chip examples
@Composable
fun ChipExamples() {
    var selectedChip by remember { mutableStateOf(0) }
    val chips = listOf("All", "Active", "Completed", "Archived")
    
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Filter by status:",
            style = MaterialTheme.typography.titleSmall
        )
        
        // Filter chips (single selection)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chips.forEachIndexed { index, chip ->
                FilterChip(
                    selected = selectedChip == index,
                    onClick = { selectedChip = index },
                    label = { Text(chip) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Input chips (removable)
        var selectedTags by remember { mutableStateOf(listOf("Android", "Kotlin")) }
        
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedTags.forEach { tag ->
                InputChip(
                    selected = true,
                    onClick = { 
                        selectedTags = selectedTags - tag
                    },
                    label = { Text(tag) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove $tag",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}

// Dialog example
@Composable
fun DialogExample() {
    var showDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Show Dialog")
        }
    }
    
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text("Confirm Action")
            },
            text = {
                Text("Are you sure you want to proceed with this action?")
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        // Handle confirmation
                        showDialog = false 
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
```

---

## Practice Projects

### Easy
1. **Simple Counter App** - Basic state management
2. **BMI Calculator** - Form handling and validation
3. **Weather Card** - Display weather information
4. **Todo List** - List management and state

### Medium
1. **Login Screen** - Form validation and navigation
2. **Recipe App** - Complex layouts and scrolling
3. **Music Player** - State management and animations
4. **Chat Interface** - Lists and input handling

### Hard
1. **E-commerce App** - Complex state and navigation
2. **Social Media Feed** - Lists, images, and interactions
3. **Game UI** - Animations and complex state
4. **Productivity App** - Multiple screens and data flow

---

**Remember**: Start with simple composables and gradually build complexity. Compose is about composition - combine small pieces to create amazing UIs! 🚀
