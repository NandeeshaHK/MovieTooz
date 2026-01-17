package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.movieapp.ui.theme.AccentCyan
import com.example.movieapp.ui.theme.PrimaryDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen {
                startActivity(Intent(this, MainActivity::class.java))
                @Suppress("DEPRECATION")
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }
}

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val text = "MOVIETOOZ"
    val animators = text.indices.map { remember { Animatable(0f) } }
    val rotationAnimators = text.indices.map { remember { Animatable(0f) } }

    // Gradient definitions for a darker, more dramatic background
    val gradientColors = listOf(
        Color.Black,
        PrimaryDark
    )

    LaunchedEffect(key1 = true) {
        // Reduced delay for faster start
        delay(100) 
        
        animators.forEachIndexed { index, animatable ->
            // Faster stagger
            delay(40) 
            
            // Launch parallel animations for bounce and rotation
            launch {
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = androidx.compose.animation.core.spring(
                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                        stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                    )
                )
            }
            // Add a random rotation kickoff that settles to 0
            launch {
                rotationAnimators[index].animateTo(
                    targetValue = 0f,
                    initialVelocity = if (index % 2 == 0) 300f else -300f, // Spin effect
                    animationSpec = androidx.compose.animation.core.spring(
                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioHighBouncy,
                        stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
                    )
                )
            }
        }
        delay(800) // Shorter hold time
        onAnimationFinished()
    }

    // High-contrast, neon-like shimmer
    val shimmerColors = listOf(
        AccentCyan,
        Color(0xFFE91E63), // Pink accent
        Color.Yellow,
        AccentCyan,
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f, // Faster shimmer
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing), // Speedy
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim),
        tileMode = TileMode.Mirror
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = gradientColors,
                    center = Offset.Unspecified,
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            text.forEachIndexed { index, char ->
                val scale = 0.5f + (animators[index].value * 0.5f) + (if (animators[index].value > 0.8f) 0.2f * kotlin.math.sin(translateAnim / 100f) else 0f).toFloat()
                
                Text(
                    text = char.toString(),
                    style = TextStyle(
                        fontSize = 48.sp, // Larger text
                        fontWeight = FontWeight.ExtraBold,
                        brush = brush
                    ),
                    modifier = Modifier
                        .offset(y = 200.dp * (1f - animators[index].value)) // Drop from higher up
                        .rotate(rotationAnimators[index].value)
                        .scale(scale)
                        .background(Color.Transparent)
                )
            }
        }
    }
}
