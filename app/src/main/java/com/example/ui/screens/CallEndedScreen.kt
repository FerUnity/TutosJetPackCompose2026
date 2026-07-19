package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
fun CallEndedScreen(
    contactName: String,
    photoUri: String,
    callDurationSeconds: Int,
    isFavorite: Boolean,
    onBackToContactsClicked: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    // Fade in animation on startup
    var startAnimation by remember { mutableStateOf(false) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800, easing = EaseOutQuad),
        label = "fade_in"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    // Convert seconds to format mm:ss
    val minutes = callDurationSeconds / 60
    val seconds = callDurationSeconds % 60
    val durationText = "%02d:%02d".format(minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SaharaLinen)
            .testTag("call_ended_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
                .alpha(contentAlpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contact Avatar circular with ambient soft shadow
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(1.dp, SaharaOutline.copy(alpha = 0.5f), CircleShape)
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
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main heading
            Text(
                text = "Llamada Finalizada",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = SaharaCharcoal,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 38.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Details card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 340.dp)
                    .border(1.dp, SaharaOutline.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SaharaCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = contactName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = SaharaCharcoal.copy(alpha = 0.8f),
                            fontFamily = FontFamily.Serif
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Timer Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = SaharaSienna,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = durationText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = SaharaSienna,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                fontSize = 20.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(SaharaOutline.copy(alpha = 0.3f))
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Contextual Outgoing & HD Rows
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.alpha(0.6f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CallMade,
                                contentDescription = null,
                                tint = SaharaCharcoal,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "SALIENTE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SaharaCharcoal,
                                    letterSpacing = 1.sp,
                                    fontSize = 10.sp
                                )
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.alpha(0.6f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Hd,
                                contentDescription = null,
                                tint = SaharaCharcoal,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "CALIDAD HD",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SaharaCharcoal,
                                    letterSpacing = 1.sp,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Primary Back to contacts CTA
            Button(
                onClick = onBackToContactsClicked,
                colors = ButtonDefaults.buttonColors(containerColor = SaharaSienna),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 280.dp)
                    .height(54.dp)
                    .testTag("back_to_contacts_btn")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Volver a Contactos",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Secondary Actions bottom: Message, Star Favorite
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { /* Message action stub */ },
                    colors = ButtonDefaults.textButtonColors(contentColor = SaharaGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mensaje",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                TextButton(
                    onClick = onToggleFavorite,
                    colors = ButtonDefaults.textButtonColors(contentColor = SaharaGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (isFavorite) SaharaRose else SaharaGray
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Favorito",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isFavorite) SaharaRose else SaharaGray
                        )
                    )
                }
            }
        }
    }
}
