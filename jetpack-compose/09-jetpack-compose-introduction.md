# Jetpack Compose Introduction - Modern Android UI Toolkit

Okay, let's start fresh.

From now on, no more WITCH/service-based company interview stuff.

We'll switch to **Jetpack Compose** — modern Android UI toolkit.

I'll explain it step by step in detail (from zero to practical level), including how it works, why it's better than old XML + View system, key concepts, and real code examples.

---

## 1. What is Jetpack Compose? (Quick Overview)

Jetpack Compose is:
- Google's **modern toolkit** for building native Android UI
- 100% **Kotlin**-based (no XML layouts anymore)
- **Declarative** UI style (you describe **what** the UI should look like → framework handles **how** to draw/update it)
- Released stable in 2021, now the **recommended** way to build Android UIs (2024–2026)

---

## 2. XML/Views vs Jetpack Compose – Main Differences

| Aspect                  | Old way (XML + View system)                  | Jetpack Compose                              |
|-------------------------|-----------------------------------------------|----------------------------------------------|
| Language                | XML + Kotlin/Java                             | Pure Kotlin                                  |
| Style                   | Imperative (findViewById, setText, etc.)      | Declarative (just return UI description)     |
| State management        | Manual (variables + setters + listeners)      | State hoisting + recomposition               |
| Performance             | View hierarchy can be deep & slow             | Flat hierarchy, smart recomposition          |
| Code amount             | Lots of boilerplate                           | Much less code                               |
| Preview                 | Static XML preview                            | Live interactive @Preview                    |
| Recommended by Google   | Legacy (still supported)                      | Future-proof, actively developed             |

---

## 3. Core Concepts of Jetpack Compose

You **must** understand these 5 things well:

1. **@Composable** functions  
   → Normal Kotlin functions annotated with `@Composable`  
   → They describe UI pieces  
   → Can call other @Composable functions

2. **Recomposition**  
   → When state changes → only affected composables are re-run (not whole screen)

3. **State** (`remember` + `mutableStateOf`)  
   → The only way to make UI "remember" values across recompositions

4. **State hoisting**  
   → Move state up to the caller (makes composables stateless & reusable)

5. **Modifiers**  
   → Like CSS classes or attributes (padding, background, clickable, size, etc.)

---

## 4. Minimal "Hello World" in Jetpack Compose

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {                    // Theme wrapper
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")     // Your UI starts here
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyAppTheme {
        Greeting("Android")
    }
}
```

---

## 5. Most Important Building Blocks (with code)

### 5.1 Layouts (instead of LinearLayout, ConstraintLayout…)

```kotlin
@Composable
fun MyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Top")
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { /* */ }) { Text("Left") }
            Button(onClick = { /* */ }) { Text("Right") }
        }
        
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Cyan),
            contentAlignment = Alignment.Center
        ) {
            Text("I'm in a Box")
        }
    }
}
```

Common layouts:
- `Column` → vertical
- `Row` → horizontal
- `Box` → stack / overlay
- `ConstraintLayout` → still exists in compose (androidx.constraintlayout.compose)

### 5.2 State – The heart of Compose

```kotlin
@Composable
fun Counter() {
    // Local state (remember + mutableStateOf)
    var count by remember { mutableIntStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { count++ }) {
            Text("Increase")
        }
    }
}
```

**Important variants of state:**

```kotlin
// 1. Simple
var name by remember { mutableStateOf("") }

// 2. With key (survives configuration change)
var count by rememberSaveable { mutableIntStateOf(0) }

// 3. Hoisted state (best practice)
@Composable
fun CounterScreen(
    count: Int,
    onCountChange: (Int) -> Unit
) {
    Button(onClick = { onCountChange(count + 1) }) {
        Text("Count: $count")
    }
}

// Parent owns state
@Composable
fun Parent() {
    var count by remember { mutableIntStateOf(0) }
    CounterScreen(count, { count = it })
}
```

### 5.3 Lists (LazyColumn – like RecyclerView)

```kotlin
@Composable
fun TodoList(todos: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(todos) { todo ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = todo,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
```

---

## 6. Next Steps – Recommended Learning Path

1. Official codelabs (best starting point)  
   https://developer.android.com/courses/pathways/jetpack-compose

2. Must-do mini projects (in this order):
   - Counter + buttons
   - Todo list (add/delete)
   - Simple login form (TextField + Button)
   - Profile screen (Image, Column, Row)
   - LazyColumn + fake API data
   - Navigation (NavHost + BottomNavigation)

3. Important libraries to learn later:
   - Navigation Compose
   - Hilt / ViewModel + StateFlow
   - Coil / Glide-Compose for images
   - Material 3 components

---

### Detailed Explanation of the Jetpack Compose Code Examples

In my previous response, I provided several code snippets to illustrate key concepts in Jetpack Compose. Below, I'll break down **each code example** line-by-line, explaining the methods, annotations, parameters, and how everything works. I'll also highlight best practices, common pitfalls, and why certain choices are made. After explaining the existing code, I'll **continue** with more advanced topics, including new code examples for inputs (TextField), images, themes, and basic navigation.

All code assumes you're using Android Studio with Jetpack Compose set up (add dependencies like `androidx.compose.ui:ui`, `androidx.compose.material3:material3`, etc., in `build.gradle`). Versions: Compose 1.6+ (as of 2026).

#### 1. Hello World Example – Breakdown

This is the minimal app setup. It shows how to integrate Compose into an Activity and create a simple composable function.

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {  // Standard Android Activity
    override fun onCreate(savedInstanceState: Bundle?) {  // Lifecycle method: called when Activity starts
        super.onCreate(savedInstanceState)  // Call parent class
        setContent {  // Key method: Sets the UI content using Compose (replaces setContentView)
            MyAppTheme {  // Applies app theme (colors, typography) – explained later
                Surface(  // Container composable: Like a div with background
                    modifier = Modifier.fillMaxSize(),  // Modifier: Chainable UI attributes (here: full screen size)
                    color = MaterialTheme.colorScheme.background  // Use theme's background color
                ) {
                    Greeting("Android")  // Call your custom composable, passing param
                }
            }
        }
    }
}

@Composable  // Annotation: Marks this as a composable function (can emit UI)
fun Greeting(name: String, modifier: Modifier = Modifier) {  // Params: name (required), modifier (optional default)
    Text(  // Built-in composable: Renders text
        text = "Hello $name!",  // String interpolation
        modifier = modifier  // Apply passed modifier (e.g., padding, click)
    )
}

@Preview(showBackground = true)  // Annotation: Enables live preview in Android Studio
@Composable
fun GreetingPreview() {  // Separate composable for preview (no Activity needed)
    MyAppTheme {
        Greeting("Android")
    }
}
```

**Detailed Breakdown**:
- **`class MainActivity : ComponentActivity()`**: This is your entry point. `ComponentActivity` is Compose-aware (unlike old `AppCompatActivity` for XML).
- **`override fun onCreate(...)`**: Android lifecycle hook. `setContent { ... }` is the magic: It takes a `@Composable` lambda as the root UI.
- **`MyAppTheme { ... }`**: A theme wrapper (generated by Android Studio wizard). It applies Material Design 3 styles (colors, fonts). Without it, UI looks plain.
- **`Surface(...)`**: A foundational composable from Material3. It's like a card/container. Parameters:
  - `modifier`: Chains attributes (e.g., `.fillMaxSize()` makes it full-screen).
  - `color`: Pulls from `MaterialTheme.colorScheme` (dynamic colors based on wallpaper in Android 12+).
  - Content lambda `{ ... }`: Children go here.
- **`Greeting(...)`**: Custom composable. `@Composable` tells Compose this function can be called in UI trees and triggers recomposition.
  - Parameters: `name` is data; `modifier` is optional (defaults to empty).
- **`Text(...)`**: Core text renderer. Parameters:
  - `text`: The content.
  - `modifier`: For styling (e.g., add `.padding(16.dp)` for space).
  - Other common params (not shown): `style = MaterialTheme.typography.bodyLarge`, `color`, `fontSize`.
- **`@Preview`**: For design-time previews. `showBackground = true` adds a white background in Studio. This function wraps your composable for isolated testing.
- **How Recomposition Works Here**: If `name` changes (e.g., via state), only `Greeting` and `Text` recompose – not the whole Activity.
- **Pitfalls**: Forget `@Composable` → compile error. Modifiers are immutable chains (e.g., `modifier.padding(8.dp).background(Color.Red)`).

Run this: You'll see "Hello Android!" centered on screen.

#### 2. Layouts Example – Breakdown

This shows how to arrange UI elements without XML constraints.

```kotlin
@Composable
fun MyScreen() {  // Root composable
    Column(  // Vertical stack (like LinearLayout vertical)
        modifier = Modifier  // Base modifier
            .fillMaxSize()   // Full screen
            .padding(16.dp), // Inner padding (dp = density-independent pixels)
        verticalArrangement = Arrangement.Center,  // Align children vertically (Center, Top, Bottom, SpaceBetween, etc.)
        horizontalAlignment = Alignment.CenterHorizontally  // Align children horizontally
    ) {
        Text("Top")  // First child

        Spacer(modifier = Modifier.height(16.dp))  // Empty space (like View with height)

        Row(  // Horizontal stack (like LinearLayout horizontal)
            horizontalArrangement = Arrangement.SpaceEvenly,  // Space children evenly
            modifier = Modifier.fillMaxWidth()  // Full width of parent
        ) {
            Button(onClick = { /* TODO: Handle click */ }) {  // Button composable
                Text("Left")  // Button content
            }
            Button(onClick = { /* */ }) { Text("Right") }
        }

        Box(  // Overlay/stack container (like FrameLayout)
            modifier = Modifier
                .size(100.dp)  // Fixed size (width + height)
                .background(Color.Cyan),  // Background color
            contentAlignment = Alignment.Center  // Align content inside
        ) {
            Text("I'm in a Box")  // Child centered
        }
    }
}
```

**Detailed Breakdown**:
- **`Column(...)`**: Stacks children vertically. Key params:
  - `modifier`: Applies to the whole column (e.g., `.padding(16.dp)` adds space around).
  - `verticalArrangement`: Controls spacing/alignment (e.g., `Arrangement.spacedBy(8.dp)` for fixed gaps).
  - `horizontalAlignment`: Aligns each child horizontally (e.g., `Alignment.Start` for left).
  - Content lambda: Children are added sequentially.
- **`Spacer(...)`**: Invisible filler. Only takes `modifier` for size (e.g., `.width(8.dp)` for horizontal space).
- **`Row(...)`**: Like Column but horizontal. Params mirror Column (e.g., `horizontalArrangement`).
- **`Button(...)`**: Material3 button. Params:
  - `onClick`: Lambda for click handling (no state change here, but usually updates state).
  - Content lambda: Usually `Text` or `Icon`.
  - Other params: `enabled = false`, `colors = ButtonDefaults.buttonColors(...)`.
- **`Box(...)`**: For overlays. Params:
  - `contentAlignment`: Positions children (e.g., `Alignment.TopEnd`).
  - Multiple children stack (last on top).
- **Modifiers Chain**: Always chain them (e.g., `.fillMaxSize().padding(16.dp)`). Order matters: Size first, then padding.
- **Pitfalls**: No weights like XML – use `Arrangement.SpaceBetween` or custom modifiers. For complex layouts, use `ConstraintLayout` in Compose.
- **Performance**: Compose optimizes flat hierarchies – avoid deep nesting.

#### 3. State Example – Breakdown

State makes UI reactive.

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableIntStateOf(0) }  // State declaration

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.headlineMedium  // Theme typography
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { count++ }) {  // Update state on click
            Text("Increase")
        }
    }
}
```

**Detailed Breakdown**:
- **`var count by remember { mutableIntStateOf(0) }`**: 
  - `mutableIntStateOf(0)`: Creates a mutable state holder (like `MutableState<Int>`).
  - `remember { ... }`: Remembers the state across recompositions (without it, state resets every time).
  - `by`: Kotlin delegate for getter/setter (e.g., `count++` updates and triggers recompose).
- **`Text(...)`**: Displays state value. When `count` changes, this `Text` recomposes.
- **`Button(onClick = { count++ })`**: Click updates state → recomposition → UI refreshes.
- **Recomposition Flow**: Click → state change → Compose smartly re-runs only `Counter` scope (not global).
- **Pitfalls**: Don't use regular `var count = 0` – it won't persist. For lists/maps, use `mutableStateListOf()` or `mutableStateMapOf()`.

**Variants Breakdown**:
- Simple: `mutableStateOf("")` for strings, etc.
- Saveable: `rememberSaveable { ... }` – survives rotation/kill (uses Bundle).
- Hoisted: Pass state down, callbacks up. Makes `CounterScreen` reusable/pure (no internal state).

#### 4. Lists Example – Breakdown

For efficient scrolling lists (replaces RecyclerView).

```kotlin
@Composable
fun TodoList(todos: List<String>) {  // Pass data as param (hoisting)
    LazyColumn(  // Lazy-loaded vertical list (loads only visible items)
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),  // Padding around list
        verticalArrangement = Arrangement.spacedBy(8.dp)  // Gap between items
    ) {
        items(todos) { todo ->  // Lambda for each item
            Card(  // Material card
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)  // Shadow
            ) {
                Text(
                    text = todo,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
```

**Detailed Breakdown**:
- **`LazyColumn(...)`**: Scrollable list. Params:
  - `contentPadding`: Adds space (top/bottom for vertical scroll).
  - `verticalArrangement`: Gaps between items.
- **`items(todos) { ... }`**: DSL builder. `todos` is data list; lambda renders each item.
  - Alternatives: `itemsIndexed` for index, `item { ... }` for single non-lazy item.
- **`Card(...)`**: Container with elevation (shadow). Params: `elevation`, `colors`, etc.
- **Performance**: Lazy – only composes visible items (great for 1000+ items).
- **Pitfalls**: Don't use regular `Column` for long lists – it composes everything (slow/OOM).
- Variants: `LazyRow` for horizontal, `LazyVerticalGrid` for grids.

### Continuing with More Topics

Now, let's build on this. I'll cover **inputs (TextField)**, **images**, **custom themes**, and **basic navigation** – common next steps.

#### 7. Inputs – TextField Example

For user input (like EditText).

```kotlin
@Composable
fun LoginForm() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(  // Material3 text input
            value = username,  // Current state
            onValueChange = { username = it },  // Update state
            label = { Text("Username") },  // Floating label
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)  // Keyboard next button
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),  // Hide text
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Validate & login */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Login")
        }
    }
}
```

**Explanation**:
- **`OutlinedTextField(...)`**: Params:
  - `value`: Bound to state.
  - `onValueChange`: Callback for every keystroke.
  - `label`: Composable for label.
  - `visualTransformation`: Masks input (e.g., for passwords).
  - `keyboardOptions`: Controls keyboard type/action.
- Integrate with ViewModel for real apps (later).

#### 8. Images – Loading & Displaying

Use Coil for async images (add dependency: `io.coil-kt:coil-compose`).

```kotlin
import coil.compose.AsyncImage

@Composable
fun ProfileScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        AsyncImage(  // Loads image async
            model = "https://example.com/image.jpg",  // URL or resource
            contentDescription = "Profile picture",  // Accessibility
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),  // Round shape
            placeholder = painterResource(R.drawable.placeholder)  // While loading
        )

        Text("User Name")
    }
}
```

**Explanation**:
- **`AsyncImage(...)`**: Handles loading, caching. Params:
  - `model`: URL, file, or `painterResource(R.drawable.ic_logo)`.
  - `contentDescription`: For screen readers.
  - `placeholder/error`: Fallback images.
- Local: Use `Image(painter = painterResource(R.drawable.my_image), ...)`.

#### 9. Custom Themes – MaterialTheme

Themes control colors/typography globally.

```kotlin
// In ui/theme/Theme.kt (generated)
@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),  // Auto dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme  // Custom schemes

    MaterialTheme(  // Applies theme
        colorScheme = colorScheme,
        typography = Typography,  // Custom fonts/sizes
        content = content
    )
}

// Custom colors (in Color.kt)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),  // Accent color
    secondary = Color(0xFF03DAC6),
    background = Color.White
)
```

**Explanation**:
- **`MaterialTheme(...)`**: Wraps app. Provides `colorScheme`, `typography`, `shapes`.
- **`isSystemInDarkTheme()`**: Detects system dark mode.
- Customize: Override `lightColorScheme`/`darkColorScheme` for colors.
- Usage: In `Text`, pull `MaterialTheme.colorScheme.primary`.

#### 10. Basic Navigation – Using Navigation Compose

For multi-screen apps (add dependency: `androidx.navigation:navigation-compose`).

```kotlin
// In MainActivity setContent
val navController = rememberNavController()  // Controller for navigation

NavHost(  // Root navigation container
    navController = navController,
    startDestination = "home"  // First screen
) {
    composable("home") {  // Route "home"
        HomeScreen(onNavigateToProfile = { navController.navigate("profile") })
    }
    composable("profile") {
        ProfileScreen(onBack = { navController.popBackStack() })
    }
}

// HomeScreen composable
@Composable
fun HomeScreen(onNavigateToProfile: () -> Unit) {
    Button(onClick = onNavigateToProfile) {
        Text("Go to Profile")
    }
}
```

**Explanation**:
- **`rememberNavController()`**: Creates nav controller (remembers across recompose).
- **`NavHost(...)`**: Hosts routes. Params: `navController`, `startDestination`.
- **`composable("route") { ... }`**: Defines screen for route. Backstack handled automatically.
- Navigation: `navController.navigate("route")` forward; `popBackStack()` back.
- Hoist callbacks for navigation (like state).
- Advanced: Pass args (`navigate("profile/$userId")`, parse in `composable { backStackEntry -> ... }`).

This covers the basics + some intermediates. Practice by building a simple app: Counter → Todo list with input → Multi-screen.

Here is a structured continuation of Jetpack Compose learning — covering **all the next logical major topics** that usually come after the basics we already discussed.

I'll explain each one in detail (concepts + why + code + common patterns + pitfalls), and provide practical, copy-paste-ready examples.

### 1. ViewModel + StateFlow + State Hoisting (Real App Pattern)

This is the **most important architecture pattern** in modern Compose apps (MVVM + unidirectional data flow).

**Key Ideas**
- UI should be **stateless** (composables don't hold state)
- State lives in **ViewModel** (survives configuration changes)
- Use **StateFlow** / **MutableStateFlow** to expose observable state
- UI collects state with `collectAsStateWithLifecycle()`

**Full Example: Counter with ViewModel**

```kotlin
// ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {

    private val _count = MutableStateFlow(0)           // private mutable
    val count: StateFlow<Int> = _count.asStateFlow()   // public read-only

    fun increment() {
        viewModelScope.launch {
            _count.value = _count.value + 1
        }
    }

    fun decrement() {
        viewModelScope.launch {
            _count.value = _count.value - 1
        }
    }

    fun reset() {
        _count.value = 0
    }
}

// Composable (UI layer)
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(
    viewModel: CounterViewModel = viewModel()   // Hilt or manual injection in real apps
) {
    val count by viewModel.count.collectAsStateWithLifecycle()   // Safe collection

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { viewModel.decrement() }) {
                Text("-")
            }
            Button(onClick = { viewModel.reset() }) {
                Text("Reset")
            }
            Button(onClick = { viewModel.increment() }) {
                Text("+")
            }
        }
    }
}
```

**Important Details**
- `MutableStateFlow` → internal mutable state
- `.asStateFlow()` → safe read-only exposure
- `collectAsStateWithLifecycle()` → automatically stops collection when UI is not visible (saves battery/CPU)
- `viewModel()` → factory that survives config changes
- In real apps → use Hilt `@HiltViewModel` + `@Inject constructor()`

**Pitfall** — never put business logic or coroutines directly in composables.

### 2. TextField + Validation + Focus Management

```kotlin
@Composable
fun LoginFormWithValidation() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(24.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                emailError = if (it.contains("@")) null else "Invalid email"
            },
            label = { Text("Email") },
            isError = emailError != null,
            supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                passwordError = if (it.length >= 6) null else "Password too short"
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Validate before submit
                if (emailError == null && passwordError == null) {
                    // proceed
                }
            },
            enabled = email.isNotBlank() && password.length >= 6,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Login")
        }
    }
}
```

**Key Points**
- `isError` + `supportingText` → show red border + error message
- `LocalFocusManager` → programmatic focus control
- `keyboardActions` + `imeAction` → better keyboard navigation (Next / Done)
- Disable button until valid → good UX

### 3. Images + Loading States (Coil)

Add dependency:  
`implementation("io.coil-kt:coil-compose:2.7.0")` (or latest)

```kotlin
@Composable
fun UserProfileImage(url: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = "User avatar",
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
        placeholder = painterResource(R.drawable.ic_placeholder),
        error = painterResource(R.drawable.ic_error),
        contentScale = ContentScale.Crop
    )
}
```

**Advanced: With loading indicator**

```kotlin
@Composable
fun AsyncProfileImage(url: String) {
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape),
            onSuccess = { isLoading = false },
            onError = { isLoading = false },
            onLoading = { isLoading = true }
        )

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}
```

### 4. Bottom Navigation + Navigation (Full Mini Example)

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val items = listOf("Home", "Profile", "Settings")

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) }, // change icon per screen
                        label = { Text(screen) },
                        selected = currentRoute == screen.lowercase(),
                        onClick = {
                            navController.navigate(screen.lowercase()) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            composable("profile") { ProfileScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}
```

**Scaffold** → provides Material Design layout structure (top bar, bottom bar, fab, drawer, etc.)

### 5. Side Effects – LaunchedEffect & DisposableEffect

```kotlin
@Composable
fun TimerScreen() {
    var seconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {  // Runs once when composable enters composition
        while (true) {
            delay(1000)
            seconds++
        }
    }

    Text("Seconds elapsed: $seconds")

    DisposableEffect(Unit) {  // Cleanup when leaving composition
        onDispose {
            println("Timer stopped")
        }
    }
}
```

**When to use**
- `LaunchedEffect(key1, key2, ...)` → run suspend code when keys change
- `DisposableEffect` → setup + cleanup (e.g., listeners, observers)

### Summary – What You Now Have

You covered:

- Activity + setContent
- Layouts (Column, Row, Box)
- State (remember, mutableStateOf, hoisting)
- LazyColumn
- TextField + validation
- Images (Coil)
- MaterialTheme
- Navigation + Scaffold + BottomNavigation
- ViewModel + StateFlow
- Side effects (LaunchedEffect)

Next realistic steps:

1. Build a **complete small app** (Todo with add/delete + persistence)
2. Add **Room database** + **Hilt** + **Repository pattern**
3. Learn **animations** (AnimatedVisibility, animateContentSize, etc.)
4. Handle **themes** (dark/light, dynamic colors)
5. Testing (Compose UI test, snapshot testing)

### Jetpack Compose Animations – Detailed Examples

Animations in Jetpack Compose are **declarative and simple** – you describe the target state, and Compose handles the transition. Key library: `androidx.compose.animation` (add to build.gradle).

**Core Concepts**:
- **AnimatedVisibility**: Show/hide with animation (fade, slide, expand).
- **animate*AsState**: Animate single values (e.g., size, color, float).
- **AnimatedContent**: Animate content change (e.g., crossfade between screens).
- **updateTransition**: For multi-state animations.
- **Modifier.animateContentSize()**: Smooth resize on content change.
- All animations are **coroutine-based** and customizable with `AnimationSpec` (spring, tween, keyframes).

**Pitfalls**: Animations run on main thread – keep them light. Use `LaunchedEffect` for side effects during animation.

#### Example 1: Basic Fade In/Out (AnimatedVisibility)

```kotlin
@Composable
fun FadeAnimationExample() {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = visible,  // Trigger: true/false
            enter = fadeIn(     // Enter animation
                animationSpec = tween(durationMillis = 1000)  // Linear 1s
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 500)   // Faster out
            )
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text("Fade Me!", modifier = Modifier.align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Hide" else "Show")
        }
    }
}
```

**Breakdown**:
- `AnimatedVisibility(visible = ..., enter = ..., exit = ...)`: Wraps content. `visible` change triggers animation.
- `fadeIn/fadeOut`: Built-in (alpha 0→1 or 1→0). Others: `slideIn/slideOut`, `expandIn/shrinkOut`.
- `tween(durationMillis = ..., easing = LinearEasing)`: Timing curve (Linear, FastOutSlowIn, etc.).
- Combine: `enter = fadeIn() + slideInVertically()` for multi-effect.

#### Example 2: Animate Value Changes (animateFloatAsState)

For smooth property changes (e.g., progress bar).

```kotlin
@Composable
fun ProgressAnimationExample() {
    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,  // Target to animate to
        animationSpec = spring(  // Physics-based (bouncy)
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Progress animation"  // For debugging
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { animatedProgress },  // Animated value
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { progress = if (progress == 1f) 0f else 1f }) {
            Text("Toggle Progress")
        }
    }
}
```

**Breakdown**:
- `animateFloatAsState(targetValue = ..., animationSpec = ...)`: Returns animated value. Variants: `animateColorAsState`, `animateDpAsState`, etc.
- `spring(...)`: Bouncy physics (dampingRatio: bounce amount, stiffness: speed).
- `tween`: Simple linear/ease.
- Use in modifiers: `Modifier.alpha(animatedProgress)`.

#### Example 3: Content Change Animation (AnimatedContent + Modifier.animateContentSize)

For lists or dynamic content.

```kotlin
@Composable
fun ListAnimationExample() {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Collapse" else "Expand")
        }

        AnimatedContent(
            targetState = expanded,  // Trigger on state change
            transitionSpec = {
                fadeIn() + expandVertically() with fadeOut() + shrinkVertically()
            },
            label = "List expand"
        ) { isExpanded ->
            if (isExpanded) {
                Column(
                    modifier = Modifier.animateContentSize(  // Smooth resize
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                    )
                ) {
                    repeat(5) {
                        Text("Item $it", modifier = Modifier.padding(8.dp))
                    }
                }
            } else {
                Text("Collapsed")
            }
        }
    }
}
```

**Breakdown**:
- `AnimatedContent(targetState = ..., transitionSpec = { ... })`: Crossfades/swaps content based on state.
- `transitionSpec`: Defines enter/exit per state change (use `initialState`/`targetState` for conditional).
- `Modifier.animateContentSize()`: Auto-animates height/width changes (e.g., expanding text).

### Compose + Retrofit + Clean Architecture – Detailed Integration

**Clean Architecture Overview** (in Android/Compose context):
- Layers: UI (Compose), Presentation (ViewModel), Domain (UseCases, Entities), Data (Repository, Retrofit/Remote, Room/Local).
- Dependency rule: Outer layers depend on inner (UI → ViewModel → UseCase → Repository).
- Benefits: Testable, modular, easy to swap data sources.

**Setup**:
- Add dependencies:
  - Retrofit: `implementation("com.squareup.retrofit2:retrofit:2.11.0")` + converter (Gson/Moshi).
  - Hilt: For DI (`dagger.hilt.android.plugin`, etc.).
- Assume API: JSONPlaceholder (todos endpoint).

**Full Example Structure**

1. **Data Layer** (Remote API with Retrofit)

```kotlin
// ApiService.kt (interface)
interface TodoApi {
    @GET("todos")
    suspend fun getTodos(): List<Todo>  // Todo data class: id, title, completed
}

// RetrofitModule.kt (Hilt module)
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideTodoApi(retrofit: Retrofit): TodoApi = retrofit.create(TodoApi::class.java)
}

// Repository.kt (Data layer)
@Singleton
class TodoRepository @Inject constructor(
    private val api: TodoApi
) {
    suspend fun fetchTodos(): List<Todo> = api.getTodos()
}
```

2. **Domain Layer** (UseCase)

```kotlin
// TodoUseCase.kt
@Singleton
class GetTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(): List<Todo> = repository.fetchTodos()
}
```

3. **Presentation Layer** (ViewModel)

```kotlin
@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadTodos()
    }

    fun loadTodos() {
        viewModelScope.launch {
            try {
                _todos.value = getTodosUseCase()
            } catch (e: Exception) {
                _error.value = "Failed to load: ${e.message}"
            }
        }
    }
}
```

4. **UI Layer** (Compose Screen)

```kotlin
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = hiltViewModel()
) {
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LazyColumn {
        if (error != null) {
            item { Text("Error: $error", color = MaterialTheme.colorScheme.error) }
        }

        items(todos) { todo ->
            Text(todo.title)
        }
    }
}
```

**Breakdown**:
- **Retrofit**: HTTP client. `@GET` defines endpoint; `suspend` for coroutines.
- **Hilt**: Injects dependencies (@Inject, @Module, @Provides).
- **UseCase**: Business logic wrapper (keeps ViewModel clean).
- **ViewModel**: Orchestrates data flow. `init { loadTodos() }` auto-loads.
- **Compose**: Collects flows → UI reacts automatically.
- **Testing**: Mock Repository/UseCase for unit tests; no UI dependencies.

**Pitfalls**: Handle errors (try-catch); use Paging3 for large lists; add local cache (Room) for offline.

### Something Else in Detail – Full Todo App with Room + Hilt

As "something else", I'll detail a **complete mini Todo app** with persistence (Room database), Hilt DI, and basic CRUD.

**Setup Dependencies**:
- Room: `androidx.room:room-runtime`, `androidx.room:room-ktx`, `androidx.room:room-compiler` (KSP).
- Hilt as above.

1. **Entity** (Domain)

```kotlin
@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val completed: Boolean = false
)
```

2. **Dao** (Data)

```kotlin
@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getAll(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)
}
```

3. **Database** (Data)

```kotlin
@Database(entities = [Todo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

// Hilt Module
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "todo_db").build()

    @Provides
    fun provideTodoDao(db: AppDatabase): TodoDao = db.todoDao()
}
```

4. **Repository** (Data)

```kotlin
@Singleton
class TodoRepository @Inject constructor(
    private val dao: TodoDao
) {
    fun getTodos(): Flow<List<Todo>> = dao.getAll()

    suspend fun addTodo(title: String) {
        dao.insert(Todo(title = title))
    }

    suspend fun deleteTodo(todo: Todo) {
        dao.delete(todo)
    }
}
```

5. **UseCases** (Domain)

```kotlin
@Singleton
class GetTodosUseCase @Inject constructor(private val repo: TodoRepository) {
    operator fun invoke(): Flow<List<Todo>> = repo.getTodos()
}

@Singleton
class AddTodoUseCase @Inject constructor(private val repo: TodoRepository) {
    suspend operator fun invoke(title: String) = repo.addTodo(title)
}

@Singleton
class DeleteTodoUseCase @Inject constructor(private val repo: TodoRepository) {
    suspend operator fun invoke(todo: Todo) = repo.deleteTodo(todo)
}
```

6. **ViewModel** (Presentation)

```kotlin
@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodos: GetTodosUseCase,
    private val addTodo: AddTodoUseCase,
    private val deleteTodo: DeleteTodoUseCase
) : ViewModel() {

    val todos: StateFlow<List<Todo>> = getTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _newTitle = MutableStateFlow("")
    val newTitle: StateFlow<String> = _newTitle.asStateFlow()

    fun updateNewTitle(title: String) {
        _newTitle.value = title
    }

    fun addNewTodo() {
        viewModelScope.launch {
            if (_newTitle.value.isNotBlank()) {
                addTodo(_newTitle.value)
                _newTitle.value = ""
            }
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            deleteTodo(todo)
        }
    }
}
```

7. **UI** (Compose Screen)

```kotlin
@Composable
fun TodoAppScreen(
    viewModel: TodoViewModel = hiltViewModel()
) {
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    val newTitle by viewModel.newTitle.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = newTitle,
            onValueChange = viewModel::updateNewTitle,
            label = { Text("New Todo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = viewModel::addNewTodo) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(todos) { todo ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(todo.title, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.deleteTodo(todo) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
```

**Breakdown**:
- **Room**: Entities + Dao + Database. Flow for reactive queries.
- **Hilt**: Injects everything.
- **ViewModel**: Uses `stateIn` to convert Flow to StateFlow.
- **UI**: Hoisted state + callbacks.
- **Full Integration**: Add to Activity: `setContent { MyAppTheme { TodoAppScreen() } }`.

**MVC vs MVP vs MVVM** are the three most historically important architectural patterns in Android development. They solve the same core problem — **how to organize code** so that UI, business logic, and data are separated — but they do it in very different ways.

In 2025–2026, the **modern Android recommendation** is clearly **MVVM** (especially when using Jetpack Compose + ViewModel + StateFlow / LiveData). MVC is legacy, MVP is rarely used anymore, and newer patterns like MVI are gaining traction in Compose apps but are still considered an evolution/extension of MVVM rather than a replacement.

### Quick Comparison Table (Android Context – 2025/2026 Reality)

| Aspect                  | MVC (Model-View-Controller)                  | MVP (Model-View-Presenter)                     | MVVM (Model-View-ViewModel)                     |
|-------------------------|----------------------------------------------|------------------------------------------------|-------------------------------------------------|
| Introduced in Android   | Default / original (pre-2015)                | ~2014–2016 (popular before Architecture Components) | 2017+ (Google official recommendation)          |
| Main components         | Model + View + Controller                    | Model + View + Presenter                       | Model + View + ViewModel                        |
| Who updates the View?   | Controller directly manipulates View         | Presenter updates View via interface           | View observes ViewModel (data binding / reactive) |
| Coupling                | Very high (Activity/Fragment = Controller + View) | Low (Presenter ↔ View via interface)           | Very low (ViewModel has no reference to View)   |
| Testability             | Poor (hard to unit test Activities)          | Good (Presenter is POJO)                       | Excellent (ViewModel is POJO + observable)      |
| Lifecycle awareness     | None built-in                                | Manual handling                                | Built-in (ViewModel survives config changes)    |
| Data binding            | Manual (setText, etc.)                       | Manual                                         | Automatic (LiveData / StateFlow / Compose state) |
| Best for                | Very small / throwaway apps                  | Medium apps where you want strict separation   | Medium–large apps, especially Jetpack Compose   |
| Current usage (2025–2026) | Legacy / maintenance only                    | Almost dead (rarely chosen new)                | Dominant (official, Compose-native)             |
| Jetpack Compose fit     | Poor (imperative style clashes)              | Possible but awkward                           | Perfect (declarative + state hoisting)          |
| Main disadvantages      | God Activities/Fragments, hard to test       | Boilerplate interfaces, manual lifecycle       | Can lead to bloated ViewModels if not careful   |

### Detailed Explanation of Each Pattern (with Android Examples)

#### 1. MVC (Model-View-Controller)

**How it works in Android (classic / flawed way)**

- **Model**: Data + business logic (Repository, Room, Retrofit calls, domain classes).
- **View**: XML layout + Activity/Fragment (displays UI).
- **Controller**: The Activity/Fragment itself — handles clicks, updates Model, then directly calls `textView.setText(...)`, `recyclerView.adapter = ...`, etc.

**Code smell example (MVC)**

```kotlin
class UserProfileActivity : AppCompatActivity() {  // ← This is both View + Controller
    private lateinit var nameText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        nameText = findViewById(R.id.tv_name)

        // Controller logic inside Activity
        lifecycleScope.launch {
            val user = repository.getUser()
            nameText.text = user.name          // Direct manipulation → tight coupling
            // more setters...
        }
    }
}
```

**Pros**  
- Very simple for tiny apps  
- No extra classes

**Cons**  
- Activities/Fragments become **Massive View Controllers** (God objects)  
- Hard to unit test (depends on Android context)  
- No lifecycle awareness for data  
- Doesn't play well with Jetpack Compose

→ Almost never used for new code in 2025–2026.

#### 2. MVP (Model-View-Presenter)

**Goal**: Fix MVC by making View passive and moving logic to a separate Presenter.

- **Model**: Same as MVC.
- **View**: Activity/Fragment — implements a View interface (only shows/hides things).
- **Presenter**: POJO class that talks to Model and calls View methods (e.g., `view.showUserName(name)`).

**Typical MVP structure**

```kotlin
// View interface
interface UserProfileView {
    fun showUserName(name: String)
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
}

// Presenter
class UserProfilePresenter(private val view: UserProfileView) {
    private val repository = UserRepository()

    fun loadUser() {
        view.showLoading()
        // Coroutine / Rx / callback
        repository.getUser { user, error ->
            view.hideLoading()
            if (user != null) {
                view.showUserName(user.name)
            } else {
                view.showError(error?.message ?: "Unknown error")
            }
        }
    }
}

// Activity (View)
class UserProfileActivity : AppCompatActivity(), UserProfileView {
    private lateinit var presenter: UserProfilePresenter

    override fun onCreate(...) {
        super.onCreate(...)
        presenter = UserProfilePresenter(this)
        presenter.loadUser()
    }

    override fun showUserName(name: String) {
        tv_name.text = name
    }
    // ... other overrides
}
```

**Pros**  
- Presenter is pure Kotlin → easy to unit test  
- View is passive → less Android dependency  
- Clear separation

**Cons**  
- Lots of boilerplate (interfaces, glue code)  
- Manual lifecycle handling (onDestroy → presenter.detachView())  
- Still imperative UI updates  
- Doesn't fit naturally with Jetpack Compose declarative style

→ Mostly legacy now. Very few new projects choose MVP in 2025+.

#### 3. MVVM (Model-View-ViewModel) – The Modern Standard

**Goal**: Decouple View completely from logic. ViewModel holds UI state and survives configuration changes.

- **Model**: Data layer (Repository, UseCases).
- **View**: Activity/Fragment or Compose screen — observes state and renders.
- **ViewModel**: Holds UI state (LiveData / StateFlow), calls Model, exposes observable data.

**Modern MVVM with Jetpack Compose**

```kotlin
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                val user = repository.getUser()
                _uiState.value = UserProfileUiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = UserProfileUiState.Error(e.message ?: "Error")
            }
        }
    }
}

// Sealed class for UI state (recommended)
sealed class UserProfileUiState {
    object Loading : UserProfileUiState()
    data class Success(val user: User) : UserProfileUiState()
    data class Error(val message: String) : UserProfileUiState()
}

// Compose Screen (View)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is UserProfileUiState.Loading -> CircularProgressIndicator()
        is UserProfileUiState.Success -> {
            val user = (uiState as UserProfileUiState.Success).user
            Text("Hello, ${user.name}")
        }
        is UserProfileUiState.Error -> Text(
            text = (uiState as UserProfileUiState.Error).message,
            color = MaterialTheme.colorScheme.error
        )
    }
}
```

**Pros**  
- ViewModel survives rotation/process death  
- Reactive data binding (no manual setters)  
- Excellent testability (ViewModel is plain class)  
- Perfect for Jetpack Compose (state hoisting + declarative UI)  
- Google official recommendation since 2017 (Architecture Components)

**Cons**  
- ViewModels can become large if not split (use multiple ViewModels or MVI)  
- Learning curve for flows/StateFlow/StateIn  
- Requires understanding of lifecycle-aware collection

### Recommendation in 2025–2026 (Android + Jetpack Compose)

| Project Type                  | Recommended Pattern       | Why?                                                                 |
|-------------------------------|----------------------------|----------------------------------------------------------------------|
| New app, Jetpack Compose      | MVVM (with StateFlow)     | Native, declarative, lifecycle-safe, testable                        |
| Very small prototype          | MVVM (even simpler)       | Still better than MVC/MVP                                            |
| Legacy XML + View system      | MVVM (with LiveData)      | Official, easy migration path                                        |
| Extremely strict unit testing | MVP (rare) or MVI         | MVP has explicit interfaces; MVI gives single source of truth        |
| Very complex state machine    | MVI (unidirectional)      | Extension of MVVM — events → reducer → single state                  |

**Bottom line (2025–2026)**

- **Forget MVC** — it's a historical mistake in Android.
- **Skip MVP** unless maintaining very old code.
- **Use MVVM** (with ViewModel + StateFlow + Compose) — this is the default, most supported, and future-proof choice.
- If your app has very complex UI state transitions → consider **MVI** on top of MVVM (single state object + intents/events).

**MVI (Model-View-Intent)** is a modern unidirectional data flow architecture pattern that has become very popular in Jetpack Compose applications (and also in Flutter, React, Redux-style systems, etc.) since ~2020–2021.

It is often described as **"MVVM done right"** or **"the natural evolution of MVVM for declarative UIs"**.

### Core Philosophy of MVI

Everything in the UI is driven by **one single immutable state object**.  
User actions produce **intents** → business logic processes them → new state is emitted → UI re-renders declaratively from that single source of truth.

Key characteristics:

- **Unidirectional data flow** (one-way street)
- **Single source of truth** (one immutable UI state)
- **Pure functions / reducers** (state + intent → new state)
- **No side effects inside View** (View only renders and sends intents)
- **Very predictable** and easy to test/debug (time-travel debugging possible)

### The Three Main Parts (M-V-I)

| Part     | Responsibility                                                                 | Android / Compose Equivalent                          | Mutable or Immutable? |
|----------|--------------------------------------------------------------------------------|--------------------------------------------------------|-----------------------|
| **Model**   | Single immutable UI state that represents **everything** the screen shows     | Sealed class / data class + StateFlow<UiState>         | Immutable             |
| **View**    | Renders the current state + collects user events and sends **Intents**        | Composable function (screen)                           | —                     |
| **Intent**  | User action or system event (button click, pull-to-refresh, page loaded, etc.)| Sealed class of events / actions                       | —                     |

### Classic MVI Flow (step by step)

1. User does something → View sends an **Intent**  
   Example: `Intent.OnAddClicked("Buy milk")`

2. ViewModel / Presenter receives Intent  
   → Calls business logic (UseCase, Repository)

3. Reducer function takes **current state + intent**  
   → Produces **new immutable state**  
   → Emits new state via StateFlow

4. View observes new state  
   → Re-renders completely from the new state (declarative)

No direct mutation of UI from anywhere except the single state → state.

### Typical MVI Structure in Jetpack Compose

```kotlin
// 1. Intents (user/system events)
sealed interface TodoIntent {
    data class AddTodo(val title: String) : TodoIntent
    data class DeleteTodo(val todo: Todo) : TodoIntent
    data object LoadTodos : TodoIntent
    data object ClearError : TodoIntent
}

// 2. Single UI State (immutable)
data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val newTodoText: String = ""
) {
    val canAdd: Boolean get() = newTodoText.isNotBlank() && !isLoading
}

// 3. ViewModel with Reducer logic
@HiltViewModel
class TodoMviViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    init {
        processIntent(TodoIntent.LoadTodos)
    }

    fun processIntent(intent: TodoIntent) {
        viewModelScope.launch {
            when (intent) {
                is TodoIntent.LoadTodos -> loadTodos()
                is TodoIntent.AddTodo -> addTodo(intent.title)
                is TodoIntent.DeleteTodo -> deleteTodo(intent.todo)
                is TodoIntent.ClearError -> clearError()
            }
        }
    }

    private suspend fun loadTodos() {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val todos = getTodosUseCase()
            _uiState.update { it.copy(todos = todos, isLoading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message, isLoading = false) }
        }
    }

    private suspend fun addTodo(title: String) {
        addTodoUseCase(title)
        _uiState.update { it.copy(newTodoText = "") }
        processIntent(TodoIntent.LoadTodos) // or optimistic update
    }

    private suspend fun deleteTodo(todo: Todo) {
        deleteTodoUseCase(todo)
        processIntent(TodoIntent.LoadTodos)
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    // Optional: text change is also an intent (keeps everything in one flow)
    fun onNewTodoTextChange(text: String) {
        _uiState.update { it.copy(newTodoText = text) }
    }
}
```

**Important pattern here**:

- State is **always updated via `.update { ... }`** → immutable copy
- All changes go through `processIntent(...)` → single entry point
- No direct setters from UI → only intents

### Compose Screen (View) – Very Clean

```kotlin
@Composable
fun TodoMviScreen(
    viewModel: TodoMviViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(16.dp)) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Button(onClick = { viewModel.processIntent(TodoIntent.ClearError) }) {
                Text("Clear Error")
            }
        }

        OutlinedTextField(
            value = state.newTodoText,
            onValueChange = viewModel::onNewTodoTextChange,
            label = { Text("New todo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.processIntent(TodoIntent.AddTodo(state.newTodoText)) },
            enabled = state.canAdd,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(state.todos) { todo ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(todo.title, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.processIntent(TodoIntent.DeleteTodo(todo)) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
```

### MVI vs MVVM – Quick Side-by-Side

| Aspect                     | Classic MVVM                              | MVI (this style)                              |
|----------------------------|-------------------------------------------|-----------------------------------------------|
| State objects              | Multiple StateFlows / LiveData            | One single UiState object                     |
| Entry point for changes    | Many public methods on ViewModel          | One method: `processIntent()`                 |
| State mutation             | Multiple places can call `.value = ...`   | Only reducer / update blocks                  |
| Debugging                  | Need to trace many flows                  | Time-travel possible (log every intent → state)|
| Boilerplate                | Less                                      | Slightly more (intent + state classes)        |
| Predictability             | Good                                      | Excellent (single source + unidirectional)    |
| Best for                   | Most apps                                 | Complex screens, form-heavy, wizards, games   |

### When to Choose MVI (2025–2026 recommendation)

**Use MVI when:**
- Screen has complex state transitions (multi-step forms, wizards, games, chat)
- You want **time-travel debugging** or very predictable behavior
- Team loves functional / Redux-style thinking
- You already use Kotlin Flows heavily

**Stick with classic MVVM when:**
- Simple list/detail screens
- You want faster development
- Team is new to Compose → less boilerplate is better

Many production apps use **hybrid**: classic MVVM for simple screens + MVI for complex ones.

**Testing MVI ViewModels with Turbine** is one of the cleanest and most powerful ways to unit-test reactive state in modern Android apps (especially with Jetpack Compose + StateFlow + MVI).

**Turbine** is a small, focused library from Cash App (app.cash.turbine) that makes testing Kotlin Flows (including StateFlow, SharedFlow, Channel) extremely readable and reliable. It solves common pain points like:

- Collecting values in the correct order
- Handling initial emission of StateFlow
- Waiting for emissions without flaky delays
- Asserting exact sequence without boilerplate

### 1. Why Turbine Fits Perfectly with MVI

In MVI:
- The **ViewModel** exposes **one immutable UiState** via `StateFlow<UiState>`
- All changes happen through a single entry point (`processIntent(...)` or `onEvent(...)`)
- You want to test that **after sending an Intent → the final state matches expectations**

Turbine lets you write tests like:

```kotlin
uiState.test {
    assertThat(awaitItem()).isEqualTo(InitialState)
    // send intent
    viewModel.processIntent(AddTodo("Milk"))
    assertThat(awaitItem()).isEqualTo(LoadingState)
    assertThat(awaitItem()).isEqualTo(SuccessState(listOf(Todo("Milk"))))
}
```

### 2. Dependencies (add to build.gradle.kts → testImplementation)

```kotlin
testImplementation("app.cash.turbine:turbine:1.2.0")   // latest as of 2026
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")  // or latest
testImplementation("junit:junit:4.13.2")               // or JUnit 5
testImplementation("io.mockk:mockk:1.13.13")           // for mocking UseCases/Repo
```

### 3. Full Practical Example – Testing an MVI Todo ViewModel

Assume this simplified MVI ViewModel (from earlier examples):

```kotlin
@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    init {
        processIntent(TodoIntent.Load)
    }

    fun processIntent(intent: TodoIntent) {
        viewModelScope.launch {
            when (intent) {
                TodoIntent.Load -> loadTodos()
                is TodoIntent.Add -> addTodo(intent.title)
            }
        }
    }

    private suspend fun loadTodos() {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val todos = getTodosUseCase()
            _uiState.update { it.copy(isLoading = false, todos = todos, error = null) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    private suspend fun addTodo(title: String) {
        _uiState.update { it.copy(isAdding = true) }
        try {
            addTodoUseCase(title)
            _uiState.update { it.copy(isAdding = false, newTodoText = "") }
            processIntent(TodoIntent.Load) // reload after add
        } catch (e: Exception) {
            _uiState.update { it.copy(isAdding = false, error = e.message) }
        }
    }
}

// State
data class TodoUiState(
    val todos: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isAdding: Boolean = false,
    val error: String? = null,
    val newTodoText: String = ""
)

// Intents
sealed interface TodoIntent {
    data object Load : TodoIntent
    data class Add(val title: String) : TodoIntent
}
```

### 4. Unit Test Examples with Turbine

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModelTest {

    // Test coroutine dispatcher (replaces Dispatchers.Main)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    // Mock dependencies
    private val getTodosUseCase: GetTodosUseCase = mockk()
    private val addTodoUseCase: AddTodoUseCase = mockk()

    // System under test
    private lateinit var viewModel: TodoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)  // Important for viewModelScope
        viewModel = TodoViewModel(getTodosUseCase, addTodoUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is emitted correctly`() = testScope.runTest {
        // No action needed - init block loads
        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(TodoUiState(isLoading = true), initial)

            // Simulate success from use case
            coEvery { getTodosUseCase() } returns listOf("Milk", "Eggs")
            advanceUntilIdle()  // let coroutines run

            val success = awaitItem()
            assertEquals(
                TodoUiState(
                    todos = listOf("Milk", "Eggs"),
                    isLoading = false
                ),
                success
            )

            expectNoEvents()  // no more emissions
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `add todo success flow`() = testScope.runTest {
        // Mock successful add
        coEvery { addTodoUseCase(any()) } just Runs
        coEvery { getTodosUseCase() } returns listOf("Milk")  // after reload

        viewModel.uiState.test {
            // Skip initial emissions
            skipItems(2)  // initial + loading

            // Send intent
            viewModel.processIntent(TodoIntent.Add("Bread"))

            // Expect intermediate adding state
            assertEquals(true, awaitItem().isAdding)

            advanceUntilIdle()

            // Final state after reload
            val final = awaitItem()
            assertEquals(false, final.isAdding)
            assertEquals("", final.newTodoText)
            assertEquals(listOf("Milk"), final.todos)  // from mocked reload

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `load fails shows error`() = testScope.runTest {
        coEvery { getTodosUseCase() } throws RuntimeException("Network error")

        viewModel.uiState.test {
            skipItems(1)  // initial loading

            val errorState = awaitItem()
            assertEquals("Network error", errorState.error)
            assertEquals(false, errorState.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `add todo failure keeps text and shows error`() = testScope.runTest {
        coEvery { addTodoUseCase(any()) } throws RuntimeException("Add failed")

        viewModel.uiState.test {
            skipItems(2)  // initial

            viewModel.processIntent(TodoIntent.Add("Invalid"))

            // Adding state
            assertEquals(true, awaitItem().isAdding)

            advanceUntilIdle()

            val errorState = awaitItem()
            assertEquals(false, errorState.isAdding)
            assertEquals("Invalid", errorState.newTodoText)  // text kept
            assertEquals("Add failed", errorState.error)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
```

### 5. Key Turbine API Used

- `.test { ... }` → starts collecting in a test coroutine
- `awaitItem()` → suspends until next emission (fails if timeout)
- `skipItems(n)` → skips initial or unwanted states
- `expectNoEvents()` → asserts no more items
- `cancelAndIgnoreRemainingEvents()` → clean up
- `advanceUntilIdle()` → runs all pending coroutines (very useful)

### 6. Best Practices & Tips (2025–2026)

- Use `@OptIn(ExperimentalCoroutinesApi::class)` + `runTest {}` from kotlinx-coroutines-test
- Always set `Dispatchers.setMain(testDispatcher)` in `@Before`
- Mock use cases with **MockK** (coEvery, coVerify)
- Test happy path + error path + loading states + edge cases
- Use `skipItems(1)` or `skipItems(2)` to ignore init/loading if predictable
- For very complex flows → use `expectMostRecentItem()` or `awaitItems(n)`
- Combine with **Truth** or **AssertJ** for nicer assertions

This style of testing gives you **100% confidence** that your MVI ViewModel behaves correctly without launching the UI.

**Clean Architecture** in Android is one of the most recommended architectural patterns in 2025–2026 when building medium to large-scale applications (especially those written in Kotlin + Jetpack Compose).

The goal of Clean Architecture is very simple in theory:

> **Business rules (domain logic) should be independent of frameworks, UI, databases, external agencies.**

That means your core business logic should **not know** anything about:
- Android (Activity, Fragment, Compose, Context)
- Retrofit, Room, Hilt/Dagger
- Coroutines / Flow / RxJava
- JSON / Gson / Moshi

This independence gives you:
- Very high **testability**
- Easy **framework migration** (change database, switch from Retrofit to Ktor, move from XML to Compose, etc.)
- Clear **separation of concerns**
- Long-term **maintainability**

### The Classic Clean Architecture Layers (Uncle Bob style)

```
                Presentation
                   ↑   ↓
                 Domain
                   ↑   ↓
                  Data
```

More detailed Android-adapted version (most common in 2025–2026):

```
                UI / Compose Screen
                      ↑   ↓
                ViewModel / StateHolder
                      ↑   ↓
                   Use Cases   ←────────┐
                      ↑   ↓               │
                   Domain / Entities      │
                      ↑   ↓               │
        ┌─────────────┴───────┴─────────────┐
        │                                     │
   Repository Interface                Repository Interface
        ↑                                     ↑
   Data Layer (Remote)                Data Layer (Local)
   (Retrofit, Ktor, etc.)            (Room, DataStore, etc.)
```

### Layer Responsibilities (2025–2026 Android Practice)

| Layer              | Contains                                      | Depends on                          | Android Dependencies? | Unit Testable? | Typical Classes                              |
|---------------------|-----------------------------------------------|-------------------------------------|------------------------|----------------|----------------------------------------------|
| **UI (Presentation)** | Jetpack Compose screens, state collection     | ViewModel                           | Yes (Compose, Navigation) | No (UI tests)  | `@Composable` functions, Screens             |
| **ViewModel**       | UI state (StateFlow), business event → intent | Use Cases                           | Minimal (ViewModel)   | Yes            | `HiltViewModel`, `StateFlow<UiState>`        |
| **Use Cases / Interactors** | Application-specific business rules           | Domain entities + Repository interfaces | None                  | Yes (core)     | `GetUserProfileUseCase`, `AddTodoUseCase`    |
| **Domain**          | Pure business entities + rules                | Nothing                             | None                  | Yes            | `data class User`, `data class Todo`, business exceptions |
| **Repository Interface** | Abstraction over data sources                 | Domain entities                     | None                  | Yes            | `interface TodoRepository`                   |
| **Data (Remote + Local)** | Implementation of repositories                | Repository interfaces + external libs | Yes (Retrofit, Room)  | Partial        | `RemoteTodoDataSource`, `LocalTodoDataSource` |

### Real-World Folder Structure (Recommended 2025–2026)

```
app/
├── di/                     # Hilt modules
├── ui/
│   ├── theme/
│   ├── navigation/
│   └── screens/            # Compose screens + ViewModels
├── feature/
│   └── todos/
│       ├── presentation/   # ViewModel + UiState + Intents (MVI) or classic MVVM
│       ├── domain/
│       │   ├── model/      # Todo, User, etc.
│       │   └── usecase/    # GetTodosUseCase, AddTodoUseCase, etc.
│       └── data/
│           ├── remote/     # TodoApi (Retrofit interface)
│           ├── local/      # TodoDao (Room)
│           └── repository/ # TodoRepositoryImpl
```

### Practical Example – Clean Architecture Todo App (MVI style)

#### 1. Domain Layer

```kotlin
// domain/model/Todo.kt
data class Todo(
    val id: Int,
    val title: String,
    val completed: Boolean
)

// domain/usecase/GetTodosUseCase.kt
class GetTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(): Result<List<Todo>> = runCatching {
        repository.getTodos()
    }
}
```

#### 2. Data Layer

```kotlin
// data/remote/TodoApi.kt
interface TodoApi {
    @GET("todos")
    suspend fun getTodos(): List<TodoDto>  // DTO = Data Transfer Object
}

// data/repository/TodoRepositoryImpl.kt
@Singleton
class TodoRepositoryImpl @Inject constructor(
    private val api: TodoApi,
    private val dao: TodoDao  // Room
) : TodoRepository {

    override suspend fun getTodos(): List<Todo> {
        // Try local first, then remote + cache
        val local = dao.getAll()
        if (local.isNotEmpty()) return local.map { it.toDomain() }

        val remote = api.getTodos().map { it.toDomain() }
        dao.insertAll(remote.map { it.toEntity() })
        return remote
    }
}
```

#### 3. Presentation Layer (ViewModel + MVI)

```kotlin
data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface TodoIntent {
    object Load : TodoIntent
}

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState(isLoading = true))
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    init { processIntent(TodoIntent.Load) }

    fun processIntent(intent: TodoIntent) {
        when (intent) {
            TodoIntent.Load -> loadTodos()
        }
    }

    private fun loadTodos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTodosUseCase()
                .onSuccess { todos -> _uiState.update { it.copy(todos = todos, isLoading = false) } }
                .onFailure { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
        }
    }
}
```

#### 4. Compose UI (completely decoupled)

```kotlin
@Composable
fun TodoScreen(viewModel: TodoViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        state.isLoading -> CircularProgressIndicator()
        state.error != null -> Text("Error: ${state.error}")
        else -> LazyColumn {
            items(state.todos) { todo ->
                Text(todo.title)
            }
        }
    }
}
```

### Why This Is Considered “Clean”

- **Domain** doesn't import anything Android or external (no Retrofit, no Room, no Flow)
- **Use Cases** depend only on **repository interface** (not implementation)
- **Repository implementation** depends on external tools (Retrofit, Room), but domain doesn't know about them
- **ViewModel** depends only on Use Cases (not on Retrofit or Room)
- **UI** depends only on ViewModel (no business logic)

### Common Variations & Modern Additions (2025–2026)

- **MVI** instead of classic MVVM (single state + intents)
- **Result / Either** instead of exceptions in Use Cases
- **Offline-first** (Room as source of truth + sync with remote)
- **Paging 3** for large lists
- **Hilt** / **Koin** for dependency injection
- **Kotlin Multiplatform** shared domain layer (iOS + Android)

### When NOT to use full Clean Architecture

- Very small apps / prototypes → simple MVVM is enough
- Extremely tight deadlines → boilerplate can slow you down
- Team is new to Kotlin / Flows → start with simpler MVVM

