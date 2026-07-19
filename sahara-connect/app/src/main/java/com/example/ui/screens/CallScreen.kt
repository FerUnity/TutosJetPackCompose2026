package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ui.theme.*

@Composable
fun CallScreen(
    contactName: String,
    photoUri: String,
    callDurationSeconds: Int,
    callState: String, // "Llamando..." or "Conectado"
    onEndCallClicked: () -> Unit
) {
    // Elegant pulsing animation for the background golden rings around avatar
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_rings")
    val ringScale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_scale_1"
    )
    val ringAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_alpha_1"
    )

    val ringScale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_scale_2"
    )
    val ringAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_alpha_2"
    )

    var muteActive by remember { mutableStateOf(false) }
    var keypadActive by remember { mutableStateOf(false) }
    var speakerActive by remember { mutableStateOf(false) }

    // Convert seconds to format mm:ss
    val minutes = callDurationSeconds / 60
    val seconds = callDurationSeconds % 60
    val durationText = "%02d:%02d".format(minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SaharaCharcoal) // Dark, warm background as specified
            .testTag("call_screen")
    ) {
        // Soft ambient background atmosphere glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SaharaSienna.copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        radius = 800f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Lock icon & encrypted call details
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .alpha(0.6f)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = SaharaSiennaLight,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CIFRADO DE EXTREMO A EXTREMO",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SaharaSiennaLight,
                        letterSpacing = 1.5.sp,
                        fontSize = 11.sp
                    )
                )
            }

            // Central Ring/Avatar Cluster
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(240.dp)
                        .padding(12.dp)
                ) {
                    // Pulsing Ring 1
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(ringScale1)
                            .alpha(ringAlpha1)
                            .border(1.dp, SaharaSienna, CircleShape)
                    )

                    // Pulsing Ring 2 (delayed offset)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(ringScale2)
                            .alpha(ringAlpha2)
                            .border(1.dp, SaharaSienna, CircleShape)
                    )

                    // Main Circular Photo
                    Box(
                        modifier = Modifier
                            .size(190.dp)
                            .clip(CircleShape)
                            .border(1.dp, SaharaOutline.copy(alpha = 0.3f), CircleShape)
                            .background(SaharaCard)
                    ) {
                        if (photoUri.isNotBlank()) {
                            Image(
                                painter = rememberAsyncImagePainter(model = photoUri),
                                contentDescription = "Foto de $contactName",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = contactName.take(1).uppercase(),
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        color = SaharaSienna.copy(alpha = 0.4f),
                                        fontWeight = FontWeight.Light
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Typography Lockup
                Text(
                    text = contactName,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = SaharaLinen,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        fontSize = 42.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Call status / Timer
                Text(
                    text = if (callState == "Conectado") durationText else "LLAMANDO...",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = SaharaSiennaLight,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }

            // Quick Actions & End Call Area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(36.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // Quick actions bottom row: Mute, Keypad, Speaker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Mute Button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { muteActive = !muteActive },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(if (muteActive) SaharaWhite else Color.Transparent)
                                .border(1.dp, SaharaOutline.copy(alpha = 0.3f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MicOff,
                                contentDescription = "Silenciar",
                                tint = if (muteActive) SaharaCharcoal else SaharaWhite
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "SILENCIAR",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SaharaOutline.copy(alpha = 0.7f),
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    // Dialpad Button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { keypadActive = !keypadActive },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(if (keypadActive) SaharaWhite else Color.Transparent)
                                .border(1.dp, SaharaOutline.copy(alpha = 0.3f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dialpad,
                                contentDescription = "Teclado",
                                tint = if (keypadActive) SaharaCharcoal else SaharaWhite
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "TECLADO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SaharaOutline.copy(alpha = 0.7f),
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    // Speaker Button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { speakerActive = !speakerActive },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(if (speakerActive) SaharaWhite else Color.Transparent)
                                .border(1.dp, SaharaOutline.copy(alpha = 0.3f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Altavoz",
                                tint = if (speakerActive) SaharaCharcoal else SaharaWhite
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "ALTAVOZ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SaharaOutline.copy(alpha = 0.7f),
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }

                // Major Action: End Call (red circular button with red glow)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(80.dp)
                ) {
                    // Soft red glow backdrop
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(1.2f)
                            .background(SaharaError.copy(alpha = 0.2f), CircleShape)
                    )

                    IconButton(
                        onClick = onEndCallClicked,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(SaharaError)
                            .testTag("end_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "Finalizar llamada",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
    }
}
