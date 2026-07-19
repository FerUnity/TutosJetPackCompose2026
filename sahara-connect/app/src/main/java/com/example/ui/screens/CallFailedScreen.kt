package com.example.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiConnectedNoInternet4
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorFilter
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
fun CallFailedScreen(
    contactName: String,
    photoUri: String,
    onRetryClicked: () -> Unit,
    onCloseClicked: () -> Unit
) {
    // Sepia color filter matrix as designed in HTML style "filter: grayscale(100%) brightness(1.1) contrast(0.9) sepia(0.2)"
    val sepiaMatrix = remember {
        ColorMatrix(
            floatArrayOf(
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SaharaLinen)
            .testTag("call_failed_screen")
    ) {
        // Decorative soft sun background glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SaharaSienna.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        radius = 1000f
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
            verticalArrangement = Arrangement.Center
        ) {
            // Visual Focus: Grayed / Sepia Profile Avatar with overlay disconnected signal icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(192.dp)
            ) {
                // Soft glowing background circle
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.1f)
                        .background(SaharaRose.copy(alpha = 0.05f), CircleShape)
                )

                // Faded avatar
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(1.dp, SaharaOutline.copy(alpha = 0.4f), CircleShape)
                        .background(SaharaCard)
                ) {
                    if (photoUri.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = photoUri),
                            contentDescription = "Foto desvanecida de $contactName",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.colorMatrix(sepiaMatrix)
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = contactName.take(1).uppercase(),
                                style = MaterialTheme.typography.displayLarge.copy(
                                    color = SaharaGray.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Light,
                                    fontSize = 48.sp
                                )
                            )
                        }
                    }

                    // Warning Icon Overlay backdrop blur style
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(SaharaLinen.copy(alpha = 0.85f))
                            .border(1.dp, SaharaOutline, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SignalWifiConnectedNoInternet4,
                            contentDescription = "Desconectado",
                            tint = SaharaRose,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Error Message Lockup
            Text(
                text = "No se pudo realizar la comunicación",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = SaharaCharcoal,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 32.sp,
                    lineHeight = 38.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 280.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Revisa tu conexión o inténtalo más tarde.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = SaharaGray,
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Contextual Actions: Reintentar & Cerrar
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Button(
                    onClick = onRetryClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = SaharaSienna),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(240.dp)
                        .height(54.dp)
                        .testTag("failed_retry_btn")
                ) {
                    Text(
                        text = "REINTENTAR",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    )
                }

                OutlinedButton(
                    onClick = onCloseClicked,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SaharaOutline),
                    modifier = Modifier
                        .width(240.dp)
                        .height(54.dp)
                        .testTag("failed_close_btn")
                ) {
                    Text(
                        text = "CERRAR",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = SaharaCharcoal,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    )
                }
            }
        }
    }
}
