package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ui.theme.*

@Composable
fun WelcomeScreen(
    onVerContactosClicked: () -> Unit
) {
    // Elegant slide-up fade-in animations on load
    val infiniteTransition = rememberInfiniteTransition(label = "welcome_bg_scale")
    val bgScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_scale"
    )

    var startAnimation by remember { mutableStateOf(false) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000, easing = EaseOutQuad),
        label = "content_alpha"
    )
    val contentOffsetY by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 20.dp,
        animationSpec = tween(1000, easing = EaseOutQuad),
        label = "content_offset_y"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("welcome_screen")
    ) {
        // Full screen background image loaded via Coil
        Image(
            painter = rememberAsyncImagePainter(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuATeWk5Vgaa3ekbJVLNi64I9BXoFwceKXPzuumJxsiFybi6_b4ILV1lQvrzWaqoNUSiQyl_TZhaQzcu_ssJ1ygK35iQfWLgiDXzh1y00BoJkV27Q5pCUjd3HTjN5S6EmVdjPdl8N0WSjoYGTcYuwKLah4d6mqrkNdns4XI34jGoVFv9PAedlx7GGDs88XtVKL22GQSSdpvbN0KW-HrXRCAd7-bKQ6zoGv-_tD-Bnvqin6T-IYa2RgfFjG0d-GuiIviiWTYPQTqX8y4"
            ),
            contentDescription = "Fondo de Bienvenida Sahara",
            modifier = Modifier
                .fillMaxSize()
                .scale(bgScale),
            contentScale = ContentScale.Crop
        )

        // Sahara golden/warm overlay to give sun-baked ambient lighting
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x1AFAF5EE), // very subtle light top
                            Color(0x4DC2652A), // warm golden sienna mid
                            Color(0xCC3A302A)  // dark warm brown bottom
                        )
                    )
                )
        )

        // Primary welcome content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .offset(y = contentOffsetY)
                    .scale(if (startAnimation) 1f else 0.98f)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Main logo header
                Text(
                    text = "Sahara Connect",
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Serif
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Editorial description
                Text(
                    text = "Artisanally crafted connections in a sun-baked world.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = SaharaLinen.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Light,
                        letterSpacing = 1.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 300.dp)
                )
            }

            // High contrast CTA button
            Button(
                onClick = onVerContactosClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SaharaSienna,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .widthIn(max = 280.dp)
                    .testTag("ver_contactos_button"),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Text(
                    text = "VER CONTACTOS",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Elegant copyright footer line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(1.dp)
                        .background(SaharaLinen.copy(alpha = 0.3f))
                )
                Text(
                    text = "JULIAN SAHARA © 2026",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SaharaLinen.copy(alpha = 0.6f),
                        letterSpacing = 3.sp
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(1.dp)
                        .background(SaharaLinen.copy(alpha = 0.3f))
                )
            }
        }
    }
}
