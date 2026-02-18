# Testing and Debugging in Jetpack Compose

Goal: Master testing strategies and debugging techniques for Compose applications.

---

## 1. Unit Testing

### What it does
Test individual composables and functions in isolation.

### Why it matters
- Reliable code
- Catch bugs early
- Refactor with confidence
- Documentation through tests

### Intuition
Unit testing is like checking individual ingredients before cooking - make sure each component works perfectly before combining them into the final dish.

### When to use
- New composable development
- Refactoring existing code
- Bug fixes
- Continuous integration

### Testing Tools
- **Compose Test Rule**: Set up test environment
- **Assertions**: Verify UI state
- **Semantics**: Test accessibility
- **Test Actions**: Simulate user interactions

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

// Simple composable to test
@Composable
fun SimpleCounter() {
    var count by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Count: $count",
            modifier = Modifier.testTag("counter_text")
        )
        
        Button(
            onClick = { count++ },
            modifier = Modifier.testTag("increment_button")
        ) {
            Text("Increment")
        }
        
        Button(
            onClick = { count = 0 },
            modifier = Modifier.testTag("reset_button")
        ) {
            Text("Reset")
        }
    }
}

// Test for SimpleCounter
class SimpleCounterTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun counter_initiallyShowsZero() {
        composeTestRule.setContent {
            SimpleCounter()
        }
        
        composeTestRule
            .onNodeWithText("Count: 0")
            .assertIsDisplayed()
    }
    
    @Test
    fun incrementButton_increasesCounter() {
        composeTestRule.setContent {
            SimpleCounter()
        }
        
        // Click increment button
        composeTestRule
            .onNodeWithText("Increment")
            .performClick()
        
        // Verify counter increased
        composeTestRule
            .onNodeWithText("Count: 1")
            .assertIsDisplayed()
    }
    
    @Test
    fun resetButton_resetsCounter() {
        composeTestRule.setContent {
            SimpleCounter()
        }
        
        // Increment first
        composeTestRule
            .onNodeWithText("Increment")
            .performClick()
        
        // Then reset
        composeTestRule
            .onNodeWithText("Reset")
            .performClick()
        
        // Verify counter is back to zero
        composeTestRule
            .onNodeWithText("Count: 0")
            .assertIsDisplayed()
    }
    
    @Test
    fun multipleIncrements_workCorrectly() {
        composeTestRule.setContent {
            SimpleCounter()
        }
        
        // Click increment button 5 times
        repeat(5) {
            composeTestRule
                .onNodeWithText("Increment")
                .performClick()
        }
        
        // Verify counter shows 5
        composeTestRule
            .onNodeWithText("Count: 5")
            .assertIsDisplayed()
    }
}

// Form composable for testing
@Composable
fun LoginForm(
    onLogin: (String, String) -> Unit = { _, _ -> }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                isError = false
            },
            label = { Text("Email") },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("email_field")
        )
        
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                isError = false
            },
            label = { Text("Password") },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password_field")
        )
        
        if (isError) {
            Text(
                text = "Please fill in all fields",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("error_message")
            )
        }
        
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    onLogin(email, password)
                } else {
                    isError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_button")
        ) {
            Text("Login")
        }
    }
}

// Test for LoginForm
class LoginFormTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun emptyFields_showError() {
        var loginCalled = false
        var loginEmail = ""
        var loginPassword = ""
        
        composeTestRule.setContent {
            LoginForm(
                onLogin = { email, password ->
                    loginCalled = true
                    loginEmail = email
                    loginPassword = password
                }
            )
        }
        
        // Click login without filling fields
        composeTestRule
            .onNodeWithText("Login")
            .performClick()
        
        // Verify error message is shown
        composeTestRule
            .onNodeWithText("Please fill in all fields")
            .assertIsDisplayed()
        
        // Verify login was not called
        assert(!loginCalled)
    }
    
    @Test
    fun validFields_callLogin() {
        var loginCalled = false
        var loginEmail = ""
        var loginPassword = ""
        
        composeTestRule.setContent {
            LoginForm(
                onLogin = { email, password ->
                    loginCalled = true
                    loginEmail = email
                    loginPassword = password
                }
            )
        }
        
        // Fill in email
        composeTestRule
            .onNodeWithTag("email_field")
            .performTextInput("test@example.com")
        
        // Fill in password
        composeTestRule
            .onNodeWithTag("password_field")
            .performTextInput("password123")
        
        // Click login
        composeTestRule
            .onNodeWithText("Login")
            .performClick()
        
        // Verify login was called with correct data
        assert(loginCalled)
        assert(loginEmail == "test@example.com")
        assert(loginPassword == "password123")
        
        // Verify error message is not shown
        composeTestRule
            .onNodeWithText("Please fill in all fields")
            .assertDoesNotExist()
    }
    
    @Test
    fun partialFields_showError() {
        var loginCalled = false
        
        composeTestRule.setContent {
            LoginForm(
                onLogin = { _, _ -> loginCalled = true }
            )
        }
        
        // Fill only email
        composeTestRule
            .onNodeWithTag("email_field")
            .performTextInput("test@example.com")
        
        // Click login
        composeTestRule
            .onNodeWithText("Login")
            .performClick()
        
        // Verify error message is shown
        composeTestRule
            .onNodeWithText("Please fill in all fields")
            .assertIsDisplayed()
        
        // Verify login was not called
        assert(!loginCalled)
    }
    
    @Test
    fun errorCleared_onInput() {
        composeTestRule.setContent {
            LoginForm()
        }
        
        // Click login to trigger error
        composeTestRule
            .onNodeWithText("Login")
            .performClick()
        
        // Verify error is shown
        composeTestRule
            .onNodeWithText("Please fill in all fields")
            .assertIsDisplayed()
        
        // Start typing in email field
        composeTestRule
            .onNodeWithTag("email_field")
            .performTextInput("test")
        
        // Verify error is cleared
        composeTestRule
            .onNodeWithText("Please fill in all fields")
            .assertDoesNotExist()
    }
}
```

---

## 2. Integration Testing

### What it does
Test multiple components working together.

### Why it matters
- Component integration
- User flow testing
- End-to-end scenarios
- Real-world usage

### Intuition
Integration testing is like testing a complete recipe - make sure all ingredients work together to create the final dish as expected.

### When to use
- Complete user flows
- Component interactions
- Navigation testing
- Complex scenarios

### Integration Techniques
- **Navigation testing**: Screen transitions
- **State testing**: Complex state flows
- **API integration**: External data
- **Database testing**: Data persistence

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.navigation.NavController
import androidx.navigation.compose.*
import org.junit.Rule
import org.junit.Test

// Todo data class
data class Todo(
    val id: String,
    val title: String,
    val isCompleted: Boolean
)

// Todo list screen
@Composable
fun TodoListScreen(
    todos: List<Todo>,
    onAddTodo: (String) -> Unit = {},
    onToggleTodo: (String) -> Unit = {},
    onDeleteTodo: (String) -> Unit = {}
) {
    var newTodoText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Add todo input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                label = { Text("New todo") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("new_todo_field")
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newTodoText.isNotBlank()) {
                        onAddTodo(newTodoText)
                        newTodoText = ""
                    }
                },
                modifier = Modifier.testTag("add_todo_button")
            ) {
                Text("Add")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Todo list
        LazyColumn(
            modifier = Modifier.testTag("todo_list"),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(todos) { todo ->
                TodoItem(
                    todo = todo,
                    onToggle = { onToggleTodo(todo.id) },
                    onDelete = { onDeleteTodo(todo.id) }
                )
            }
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("todo_item_${todo.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggle() },
                modifier = Modifier.testTag("todo_checkbox_${todo.id}")
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = todo.title,
                modifier = Modifier
                    .weight(1f)
                    .testTag("todo_title_${todo.id}")
            )
            
            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("delete_button_${todo.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

// Integration test for TodoListScreen
class TodoListIntegrationTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    private val initialTodos = listOf(
        Todo("1", "Buy groceries", false),
        Todo("2", "Walk the dog", true),
        Todo("3", "Read book", false)
    )
    
    @Test
    fun todoList_displaysInitialTodos() {
        composeTestRule.setContent {
            TodoListScreen(todos = initialTodos)
        }
        
        // Verify all todos are displayed
        initialTodos.forEach { todo ->
            composeTestRule
                .onNodeWithText(todo.title)
                .assertIsDisplayed()
        }
    }
    
    @Test
    fun addTodo_addsNewTodoToList() {
        var addedTodo = ""
        val updatedTodos = mutableListOf<Todo>()
        
        composeTestRule.setContent {
            TodoListScreen(
                todos = updatedTodos,
                onAddTodo = { title ->
                    addedTodo = title
                    updatedTodos.add(
                        Todo(
                            id = (updatedTodos.size + 1).toString(),
                            title = title,
                            isCompleted = false
                        )
                    )
                }
            )
        }
        
        // Add new todo
        composeTestRule
            .onNodeWithTag("new_todo_field")
            .performTextInput("New task")
        
        composeTestRule
            .onNodeWithTag("add_todo_button")
            .performClick()
        
        // Verify todo was added
        assert(addedTodo == "New task")
        assert(updatedTodos.size == 1)
        
        composeTestRule
            .onNodeWithText("New task")
            .assertIsDisplayed()
    }
    
    @Test
    fun toggleTodo_updatesCompletionStatus() {
        var toggledTodoId = ""
        val updatedTodos = initialTodos.toMutableList()
        
        composeTestRule.setContent {
            TodoListScreen(
                todos = updatedTodos,
                onToggleTodo = { id ->
                    toggledTodoId = id
                    val index = updatedTodos.indexOfFirst { it.id == id }
                    if (index >= 0) {
                        updatedTodos[index] = updatedTodos[index].copy(isCompleted = !updatedTodos[index].isCompleted)
                    }
                }
            )
        }
        
        // Toggle first todo
        composeTestRule
            .onNodeWithTag("todo_checkbox_1")
            .performClick()
        
        // Verify todo was toggled
        assert(toggledTodoId == "1")
        assert(updatedTodos[0].isCompleted == true)
    }
    
    @Test
    fun deleteTodo_removesFromList() {
        var deletedTodoId = ""
        val updatedTodos = initialTodos.toMutableList()
        
        composeTestRule.setContent {
            TodoListScreen(
                todos = updatedTodos,
                onDeleteTodo = { id ->
                    deletedTodoId = id
                    updatedTodos.removeAll { it.id == id }
                }
            )
        }
        
        // Delete second todo
        composeTestRule
            .onNodeWithTag("delete_button_2")
            .performClick()
        
        // Verify todo was deleted
        assert(deletedTodoId == "2")
        assert(updatedTodos.size == 2)
        assert(!updatedTodos.any { it.id == "2" })
        
        // Verify todo is no longer displayed
        composeTestRule
            .onNodeWithText("Walk the dog")
            .assertDoesNotExist()
    }
    
    @Test
    fun completeUserFlow_addToggleDelete() {
        val todos = mutableListOf<Todo>()
        var addedTitle = ""
        var toggledId = ""
        var deletedId = ""
        
        composeTestRule.setContent {
            TodoListScreen(
                todos = todos,
                onAddTodo = { title ->
                    addedTitle = title
                    todos.add(
                        Todo(
                            id = (todos.size + 1).toString(),
                            title = title,
                            isCompleted = false
                        )
                    )
                },
                onToggleTodo = { id ->
                    toggledId = id
                    val index = todos.indexOfFirst { it.id == id }
                    if (index >= 0) {
                        todos[index] = todos[index].copy(isCompleted = !todos[index].isCompleted)
                    }
                },
                onDeleteTodo = { id ->
                    deletedId = id
                    todos.removeAll { it.id == id }
                }
            )
        }
        
        // Step 1: Add todo
        composeTestRule
            .onNodeWithTag("new_todo_field")
            .performTextInput("Complete integration test")
        
        composeTestRule
            .onNodeWithTag("add_todo_button")
            .performClick()
        
        // Verify added
        assert(addedTitle == "Complete integration test")
        assert(todos.size == 1)
        
        // Step 2: Toggle todo
        composeTestRule
            .onNodeWithTag("todo_checkbox_1")
            .performClick()
        
        // Verify toggled
        assert(toggledId == "1")
        assert(todos[0].isCompleted == true)
        
        // Step 3: Delete todo
        composeTestRule
            .onNodeWithTag("delete_button_1")
            .performClick()
        
        // Verify deleted
        assert(deletedId == "1")
        assert(todos.isEmpty())
        
        // Verify todo is gone from UI
        composeTestRule
            .onNodeWithText("Complete integration test")
            .assertDoesNotExist()
    }
}
```

---

## 3. UI Testing

### What it does
Test user interactions and UI behavior from user perspective.

### Why it matters
- User experience validation
- Accessibility testing
- Visual regression testing
- Cross-device testing

### Intuition
UI testing is like having a robot user test your app - it performs the same actions a real user would and verifies the results.

### When to use
- Critical user flows
- Accessibility compliance
- Visual consistency
- Multi-device testing

### UI Testing Tools
- **UI Automator**: System-level testing
- **Espresso**: View-based testing
- **Compose Test**: Compose-specific testing
- **Accessibility Tests**: Screen reader support

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

// Shopping cart composable
@Composable
fun ShoppingCart(
    items: List<CartItem>,
    onRemoveItem: (String) -> Unit = {},
    onCheckout: () -> Unit = {}
) {
    val total = items.sumOf { it.price * it.quantity }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Shopping Cart",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag("cart_title")
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (items.isEmpty()) {
            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.testTag("empty_cart_message")
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .testTag("cart_items"),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    CartItem(
                        item = item,
                        onRemove = { onRemoveItem(item.id) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.testTag("cart_total")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("checkout_button")
            ) {
                Text("Checkout")
            }
        }
    }
}

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

@Composable
fun CartItem(
    item: CartItem,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cart_item_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.testTag("item_name_${item.id}")
                )
                Text(
                    text = "$${String.format("%.2f", item.price)} x ${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag("item_price_${item.id}")
                )
            }
            
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .testTag("remove_button_${item.id}")
                    .semantics {
                        contentDescription = "Remove ${item.name} from cart"
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove"
                )
            }
        }
    }
}

// UI Test for ShoppingCart
class ShoppingCartUITest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    private val sampleItems = listOf(
        CartItem("1", "Laptop", 999.99, 1),
        CartItem("2", "Mouse", 29.99, 2),
        CartItem("3", "Keyboard", 79.99, 1)
    )
    
    @Test
    fun emptyCart_showsEmptyMessage() {
        composeTestRule.setContent {
            ShoppingCart(items = emptyList())
        }
        
        composeTestRule
            .onNodeWithTag("cart_title")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithTag("empty_cart_message")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("Your cart is empty")
            .assertIsDisplayed()
        
        // Verify cart items and checkout are not shown
        composeTestRule
            .onNodeWithTag("cart_items")
            .assertDoesNotExist()
        
        composeTestRule
            .onNodeWithTag("checkout_button")
            .assertDoesNotExist()
    }
    
    @Test
    fun cartWithItems_displaysCorrectly() {
        composeTestRule.setContent {
            ShoppingCart(items = sampleItems)
        }
        
        // Verify title
        composeTestRule
            .onNodeWithTag("cart_title")
            .assertIsDisplayed()
        
        // Verify items are displayed
        sampleItems.forEach { item ->
            composeTestRule
                .onNodeWithTag("cart_item_${item.id}")
                .assertIsDisplayed()
            
            composeTestRule
                .onNodeWithText(item.name)
                .assertIsDisplayed()
            
            composeTestRule
                .onNodeWithTag("item_name_${item.id}")
                .assertIsDisplayed()
        }
        
        // Verify total is calculated correctly
        val expectedTotal = sampleItems.sumOf { it.price * it.quantity }
        composeTestRule
            .onNodeWithText("$${String.format("%.2f", expectedTotal)}")
            .assertIsDisplayed()
        
        // Verify checkout button is shown
        composeTestRule
            .onNodeWithTag("checkout_button")
            .assertIsDisplayed()
    }
    
    @Test
    void removeItem_updatesCartAndTotal() {
        var removedItemId = ""
        val items = sampleItems.toMutableList()
        
        composeTestRule.setContent {
            ShoppingCart(
                items = items,
                onRemoveItem = { id ->
                    removedItemId = id
                    items.removeAll { it.id == id }
                }
            )
        }
        
        // Verify initial total
        val initialTotal = items.sumOf { it.price * it.quantity }
        composeTestRule
            .onNodeWithText("$${String.format("%.2f", initialTotal)}")
            .assertIsDisplayed()
        
        // Remove second item
        composeTestRule
            .onNodeWithTag("remove_button_2")
            .performClick()
        
        // Verify item was removed
        assert(removedItemId == "2")
        assert(items.size == 2)
        
        // Verify item is no longer displayed
        composeTestRule
            .onNodeWithTag("cart_item_2")
            .assertDoesNotExist()
        
        composeTestRule
            .onNodeWithText("Mouse")
            .assertDoesNotExist()
        
        // Verify total was updated
        val newTotal = items.sumOf { it.price * it.quantity }
        composeTestRule
            .onNodeWithText("$${String.format("%.2f", newTotal)}")
            .assertIsDisplayed()
    }
    
    @Test
    fun checkoutButton_triggersCheckout() {
        var checkoutCalled = false
        
        composeTestRule.setContent {
            ShoppingCart(
                items = sampleItems,
                onCheckout = { checkoutCalled = true }
            )
        }
        
        // Click checkout
        composeTestRule
            .onNodeWithTag("checkout_button")
            .performClick()
        
        // Verify checkout was called
        assert(checkoutCalled)
    }
    
    @Test
    fun accessibility_contentDescriptions() {
        composeTestRule.setContent {
            ShoppingCart(items = sampleItems)
        }
        
        // Verify remove buttons have proper content descriptions
        sampleItems.forEach { item ->
            composeTestRule
                .onNodeWithContentDescription("Remove ${item.name} from cart")
                .assertIsDisplayed()
        }
    }
    
    @Test
    fun scrollBehavior_worksCorrectly() {
        val manyItems = (1..50).map { index ->
            CartItem(
                id = index.toString(),
                name = "Item $index",
                price = 10.0 * index,
                quantity = 1
            )
        }
        
        composeTestRule.setContent {
            ShoppingCart(items = manyItems)
        }
        
        // Verify first item is visible
        composeTestRule
            .onNodeWithText("Item 1")
            .assertIsDisplayed()
        
        // Scroll to bottom
        composeTestRule
            .onNodeWithTag("cart_items")
            .performScrollTo()
        
        // Verify last item is visible
        composeTestRule
            .onNodeWithText("Item 50")
            .assertIsDisplayed()
    }
}
```

---

## 4. Debugging Techniques

### What it does
Identify and fix issues in Compose applications.

### Why it matters
- Faster bug resolution
- Better code quality
- Understanding Compose behavior
- Performance optimization

### Intuition
Debugging is like being a detective - gather clues, analyze evidence, and find the root cause of the problem.

### When to use
- Unexpected behavior
- Performance issues
- Visual bugs
- State problems

### Debugging Tools
- **Compose Inspector**: Visual debugging
- **Layout Inspector**: Layout analysis
- **Logcat**: Runtime debugging
- **Breakpoints**: Step-through debugging

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

// Debuggable composable with logging
@Composable
fun DebuggableCounter() {
    var count by remember { mutableStateOf(0) }
    
    // Debug logging
    LaunchedEffect(count) {
        println("Debug: Count changed to $count")
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Count: $count",
            modifier = Modifier.testTag("counter_text")
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    println("Debug: Increment button clicked")
                    count++
                },
                modifier = Modifier.testTag("increment_button")
            ) {
                Text("Increment")
            }
            
            Button(
                onClick = {
                    println("Debug: Decrement button clicked")
                    count--
                },
                modifier = Modifier.testTag("decrement_button")
            ) {
                Text("Decrement")
            }
        }
        
        Button(
            onClick = {
                println("Debug: Reset button clicked")
                count = 0
            },
            modifier = Modifier.testTag("reset_button")
        ) {
            Text("Reset")
        }
    }
}

// State debugging composable
@Composable
fun StateDebugger() {
    var text by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    
    // Debug state changes
    LaunchedEffect(text) {
        println("Debug: Text changed to '$text'")
    }
    
    LaunchedEffect(isEditing) {
        println("Debug: Editing state changed to $isEditing")
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                println("Debug: TextField onValueChange called with '$newText'")
                text = newText
            },
            label = { Text("Enter text") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Button(
            onClick = { isEditing = !isEditing },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditing) "Stop Editing" else "Start Editing")
        }
        
        // Debug information display
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Debug Information:", style = MaterialTheme.typography.titleSmall)
                Text("Text: '$text'")
                Text("Text length: ${text.length}")
                Text("Is editing: $isEditing")
                Text("Text hash: ${text.hashCode()}")
            }
        }
    }
}

// Performance debugging composable
@Composable
fun PerformanceDebugger() {
    var recompositionCount by remember { mutableStateOf(0) }
    
    // Track recompositions
    LaunchedEffect(Unit) {
        recompositionCount++
        println("Debug: Composable recomposed $recompositionCount times")
    }
    
    var counter by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Recompositions: $recompositionCount",
            style = MaterialTheme.typography.titleMedium
        )
        
        Text(
            text = "Counter: $counter",
            style = MaterialTheme.typography.titleMedium
        )
        
        Button(
            onClick = {
                println("Debug: Button clicked, incrementing counter")
                counter++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Increment Counter")
        }
        
        // Performance tips
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Performance Tips:", style = MaterialTheme.typography.titleSmall)
                Text("• Use remember for expensive calculations")
                Text("• Use derivedStateOf for computed state")
                Text("• Use stable data classes")
                Text("• Avoid unnecessary recompositions")
                Text("• Use keys in LazyColumn items")
            }
        }
    }
}

// Layout debugging composable
@Composable
fun LayoutDebugger() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Layout Debugging",
            style = MaterialTheme.typography.headlineSmall
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Debug different layout scenarios
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Nested Layouts:", style = MaterialTheme.typography.titleSmall)
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text("Nested")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Layout")
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Layout Tips:", style = MaterialTheme.typography.titleSmall)
                Text("• Use Layout Inspector to debug")
                Text("• Minimize nesting depth")
                Text("• Use proper modifiers")
                Text("• Test with different screen sizes")
                Text("• Check for unnecessary recompositions")
            }
        }
    }
}

// Preview for debugging
@Preview(showBackground = true)
@Composable
fun DebuggableCounterPreview() {
    MaterialTheme {
        DebuggableCounter()
    }
}

@Preview(showBackground = true)
@Composable
fun StateDebuggerPreview() {
    MaterialTheme {
        StateDebugger()
    }
}

// Preview parameter provider for testing different states
class CounterPreviewParameterProvider : PreviewParameterProvider<Int> {
    override val values = sequenceOf(0, 5, 10, 100)
}

@Preview(showBackground = true)
@Composable
fun CounterWithDifferentValues(
    @PreviewParameter(CounterPreviewParameterProvider::class) count: Int
) {
    var currentCount by remember { mutableStateOf(count) }
    
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text("Count: $currentCount")
        Button(onClick = { currentCount++ }) {
            Text("Increment")
        }
    }
}
```

---

## Practice Projects

### Easy
1. **Unit Test Counter** - Basic composable testing
2. **Form Validation Test** - Input validation testing
3. **Button Click Test** - Simple interaction testing

### Medium
1. **Todo App Tests** - Complete CRUD testing
2. **Navigation Tests** - Screen transition testing
3. **State Management Tests** - Complex state testing

### Hard
1. **E-commerce App Tests** - End-to-end testing
2. **Accessibility Tests** - Screen reader testing
3. **Performance Tests** - Optimization testing

---

**Remember**: Good tests are your safety net - they catch bugs early and give you confidence to make changes! 🚀
