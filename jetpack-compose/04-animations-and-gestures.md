# Animations and Gestures in Jetpack Compose

Goal: Master animations and gesture handling for engaging user interfaces.

---

## 1. Animation Basics

### What it does
Create smooth transitions and visual effects in Compose applications.

### Why it matters
- Better user experience
- Visual feedback
- Modern UI feel
- User engagement

### Intuition
Animations are like special effects for your app - they make transitions smooth and provide visual feedback. Think of them as the difference between a flipbook and a smooth video.

### When to use
- Screen transitions
- Loading states
- Interactive feedback
- State changes
- User interactions

### Animation Types
- **Value-based**: Animate numeric values
- **Color-based**: Animate color transitions
- **Size-based**: Animate dimensions
- **Position-based**: Animate movement
- **Visibility-based**: Fade in/out effects

### Java/Kotlin Code
```kotlin
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Basic animation with animateFloatAsState
@Composable
fun BasicAnimationExample() {
    var isExpanded by remember { mutableStateOf(false) }
    
    // Animate size change
    val size by animateFloatAsState(
        targetValue = if (isExpanded) 200f else 100f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )
    
    // Animate alpha
    val alpha by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0.5f,
        animationSpec = tween(durationMillis = 800)
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .background(
                    Color.Blue,
                    RoundedCornerShape(if (isExpanded) 32.dp else 16.dp)
                )
                .clip(RoundedCornerShape(if (isExpanded) 32.dp else 16.dp))
                .alpha(alpha),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isExpanded) "Expanded" else "Compact",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        Button(
            onClick = { isExpanded = !isExpanded }
        ) {
            Text(if (isExpanded) "Compact" else "Expand")
        }
    }
}

// Color animation
@Composable
fun ColorAnimationExample() {
    var isPrimary by remember { mutableStateOf(true) }
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isPrimary) 
            MaterialTheme.colorScheme.primary 
        else 
            MaterialTheme.colorScheme.secondary,
        animationSpec = tween(durationMillis = 1000)
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isPrimary) Color.White else Color.Black,
        animationSpec = tween(durationMillis = 1000)
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Card(
            modifier = Modifier
                .size(200.dp)
                .background(backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Color Animation",
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        
        Button(
            onClick = { isPrimary = !isPrimary }
        ) {
            Text("Switch Color")
        }
    }
}

// Size animation with different specs
@Composable
fun SizeAnimationExample() {
    var animationType by remember { mutableStateOf(0) }
    
    val size by when (animationType) {
        0 -> animateFloatAsState(
            targetValue = 100f,
            animationSpec = tween(durationMillis = 500)
        )
        1 -> animateFloatAsState(
            targetValue = 150f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        else -> animateFloatAsState(
            targetValue = 200f,
            animationSpec = keyframes {
                durationMillis = 1000
                0f at 0 with LinearEasing
                250f at 500 with FastOutSlowInEasing
                200f at 1000
            }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${size.toInt()}dp",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { animationType = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (animationType == 0) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Text("Tween")
            }
            
            Button(
                onClick = { animationType = 1 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (animationType == 1) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Text("Spring")
            }
            
            Button(
                onClick = { animationType = 2 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (animationType == 2) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Text("Keyframes")
            }
        }
    }
}
```

---

## 2. Advanced Animations

### What it does
Create complex animation sequences and transitions.

### Why it matters
- Sophisticated user interfaces
- Micro-interactions
- Storytelling through motion
- Professional polish

### Intuition
Advanced animations are like choreography - they coordinate multiple elements moving together in perfect sync, creating a beautiful visual performance.

### When to use
- Complex state transitions
- Multiple element animations
- Choreographed effects
- Loading animations
- Success/error feedback

### Animation Techniques
- **AnimatedVisibility**: Fade and slide transitions
- **Crossfade**: Switch between content
- **AnimatedContent**: Content transitions
- **InfiniteTransition**: Continuous animations
- **Transition**: Coordinate multiple animations

### Java/Kotlin Code
```kotlin
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

// AnimatedVisibility example
@Composable
fun AnimatedVisibilityExample() {
    var visible by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            ) + slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 1000)
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 500)
            ) + slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 500)
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Animated Content",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
        
        Button(
            onClick = { visible = !visible }
        ) {
            Text(if (visible) "Hide" else "Show")
        }
    }
}

// Crossfade example
@Composable
fun CrossfadeExample() {
    var currentPage by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Crossfade(
            targetState = currentPage,
            animationSpec = tween(durationMillis = 500)
        ) { page ->
            when (page) {
                0 -> PageOne()
                1 -> PageTwo()
                2 -> PageThree()
            }
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { currentPage = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentPage == 0) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Text("Page 1")
            }
            
            Button(
                onClick = { currentPage = 1 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentPage == 1) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Text("Page 2")
            }
            
            Button(
                onClick = { currentPage = 2 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentPage == 2) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Text("Page 3")
            }
        }
    }
}

@Composable
fun PageOne() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Page 1",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun PageTwo() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Page 2",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun PageThree() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Page 3",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

// Infinite animation example
@Composable
fun InfiniteAnimationExample() {
    val infiniteTransition = rememberInfiniteTransition()
    
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Loading",
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Text(
            text = "Loading...",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

// Complex animation with transition
@Composable
fun ComplexAnimationExample() {
    var isPlaying by remember { mutableStateOf(false) }
    
    val transition = updateTransition(isPlaying, label = "playTransition")
    
    val backgroundColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "backgroundColor"
    ) { playing ->
        if (playing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    }
    
    val iconRotation by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "iconRotation"
    ) { playing ->
        if (playing) 360f else 0f
    }
    
    val buttonScale by transition.animateFloat(
        transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy) },
        label = "buttonScale"
    ) { playing ->
        if (playing) 1.1f else 1f
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Card(
            modifier = Modifier
                .size(200.dp)
                .background(backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier
                        .size(80.dp)
                        .scale(buttonScale),
                    tint = Color.White
                )
            }
        }
        
        Button(
            onClick = { isPlaying = !isPlaying },
            modifier = Modifier.scale(buttonScale)
        ) {
            Text(if (isPlaying) "Pause" else "Play")
        }
    }
}

// Loading animation with multiple elements
@Composable
fun LoadingAnimationExample() {
    val infiniteTransition = rememberInfiniteTransition()
    
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            initialOffsetMillis = 200,
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val alpha3 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            initialOffsetMillis = 400,
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = alpha1),
                    CircleShape
                )
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = alpha2),
                    CircleShape
                )
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = alpha3),
                    CircleShape
                )
        )
    }
}
```

---

## 3. Gesture Handling

### What it does
Detect and respond to user touch interactions.

### Why it matters
- Interactive user interfaces
- Custom gesture recognition
- Touch feedback
- User engagement

### Intuition
Gestures are like the language of touch - they translate finger movements into actions. Think of them as interpreters between what users do and what your app should respond to.

### When to use
- Custom interactions
- Swipe gestures
- Pinch to zoom
- Drag and drop
- Long press actions

### Gesture Types
- **Tap**: Single touch
- **Double Tap**: Two quick taps
- **Long Press**: Hold gesture
- **Drag**: Move while touching
- **Swipe**: Quick directional movement
- **Pinch**: Two-finger zoom

### Java/Kotlin Code
```kotlin
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

// Basic tap and click gestures
@Composable
fun TapGestureExample() {
    var tapCount by remember { mutableStateOf(0) }
    var lastAction by remember { mutableStateOf("Waiting for tap...") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Tap Count: $tapCount",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            text = lastAction,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Card(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            tapCount++
                            lastAction = "Single tap detected!"
                        },
                        onDoubleTap = {
                            tapCount += 2
                            lastAction = "Double tap detected!"
                        },
                        onLongPress = {
                            tapCount += 5
                            lastAction = "Long press detected!"
                        }
                    )
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = "Touch",
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
        
        Button(
            onClick = { 
                tapCount = 0
                lastAction = "Counter reset!"
            }
        ) {
            Text("Reset")
        }
    }
}

// Drag gesture example
@Composable
fun DragGestureExample() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Drag the box around!",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            text = "Position: (${offsetX.toInt()}, ${offsetY.toInt()})",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                isDragging = true
                            },
                            onDragEnd = {
                                isDragging = false
                            }
                        ) { change, dragAmount ->
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .graphicsLayer {
                        if (isDragging) {
                            scaleX = 1.1f
                            scaleY = 1.1f
                        }
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isDragging) 12.dp else 4.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isDragging) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DragIndicator,
                        contentDescription = "Drag",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        
        Button(
            onClick = { 
                offsetX = 0f
                offsetY = 0f
            }
        ) {
            Text("Reset Position")
        }
    }
}

// Swipe gesture example
@Composable
fun SwipeGestureExample() {
    var swipeDirection by remember { mutableStateOf("Swipe anywhere!") }
    var cardColor by remember { mutableStateOf(MaterialTheme.colorScheme.primary) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = swipeDirection,
            style = MaterialTheme.typography.headlineMedium
        )
        
        Card(
            modifier = Modifier
                .size(200.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            // Reset after swipe
                            cardColor = MaterialTheme.colorScheme.primary
                        }
                    ) { change, dragAmount ->
                        val threshold = 50f
                        
                        when {
                            dragAmount.x > threshold -> {
                                swipeDirection = "Swiped Right!"
                                cardColor = Color.Green
                            }
                            dragAmount.x < -threshold -> {
                                swipeDirection = "Swiped Left!"
                                cardColor = Color.Red
                            }
                            dragAmount.y > threshold -> {
                                swipeDirection = "Swiped Down!"
                                cardColor = Color.Blue
                            }
                            dragAmount.y < -threshold -> {
                                swipeDirection = "Swiped Up!"
                                cardColor = Color.Yellow
                            }
                        }
                    }
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(cardColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Swipe,
                    contentDescription = "Swipe",
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

// Transform gesture (scale and rotation)
@Composable
fun TransformGestureExample() {
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Pinch to scale, drag to rotate!",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            text = "Scale: ${scale.format(2)}, Rotation: ${rotation.toInt()}°",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Card(
            modifier = Modifier
                .size(200.dp)
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, rotationDelta ->
                        scale *= zoom
                        scale = scale.coerceIn(0.5f, 3f)
                        rotation += rotationDelta
                    }
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ZoomOutMap,
                    contentDescription = "Transform",
                    tint = Color.White,
                    modifier = Modifier
                        .size(64.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            rotationZ = rotation
                        }
                )
            }
        }
        
        Button(
            onClick = { 
                scale = 1f
                rotation = 0f
            }
        ) {
            Text("Reset")
        }
    }
}

// Combined gesture example
@Composable
fun CombinedGestureExample() {
    var position by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var isLongPressed by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Long press, drag, and pinch!",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .offset { IntOffset(position.x.toInt(), position.y.toInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { 
                                isLongPressed = false
                            }
                        ) { change, dragAmount ->
                            position += Offset(dragAmount.x, dragAmount.y)
                            
                            // Keep within bounds
                            val maxX = 100f
                            val maxY = 100f
                            position = position.coerceIn(
                                Offset(-maxX, -maxY),
                                Offset(maxX, maxY)
                            )
                        }
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, rotationDelta ->
                            scale *= zoom
                            scale = scale.coerceIn(0.5f, 2f)
                            rotation += rotationDelta
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                isLongPressed = true
                                position = Offset.Zero
                                scale = 1f
                                rotation = 0f
                            }
                        )
                    }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationZ = rotation
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isLongPressed) 16.dp else 8.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isLongPressed) 
                                MaterialTheme.colorScheme.error 
                            else 
                                MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLongPressed) 
                            Icons.Default.Favorite 
                        else 
                            Icons.Default.PanTool,
                        contentDescription = "Gesture",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        
        Text(
            text = if (isLongPressed) "Long pressed! Reset to center." else "Try long press to reset",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Helper function to format float values
private fun Float.format(digits: Int): String {
    return "%.${digits}f".format(this)
}
```

---

## Practice Projects

### Easy
1. **Animated Button** - Basic hover and click animations
2. **Loading Spinner** - Simple rotation animation
3. **Tap Counter** - Gesture detection with feedback

### Medium
1. **Swipeable Cards** - Tinder-like swipe gestures
2. **Animated List Items** - List with enter/exit animations
3. **Pinch to Zoom** - Image viewer with zoom gestures

### Hard
1. **Gesture-Based Game** - Complex gesture recognition
2. **Animated Onboarding** - Multi-screen animated flow
3. **Interactive Dashboard** - Charts with animations and gestures

---

**Remember**: Animations and gestures make your app feel alive and responsive. Use them thoughtfully to enhance user experience! 🚀
