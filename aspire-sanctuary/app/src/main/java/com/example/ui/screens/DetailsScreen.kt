package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Property
import com.example.ui.AppViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToBookings: () -> Unit
) {
    val property by viewModel.selectedProperty.collectAsState()

    var showBookingSheet by remember { mutableStateOf(false) }
    var checkInDate by remember { mutableStateOf("Dec 14, 2024") }
    var checkOutDate by remember { mutableStateOf("Dec 21, 2024") }
    var guestsCount by remember { mutableStateOf(2) }
    var bookingCompleted by remember { mutableStateOf(false) }

    if (property == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = GoldAccent)
        }
        return
    }

    val currentProperty = property!!
    val images = currentProperty.imageUrlList

    val pagerState = rememberPagerState(pageCount = { images.size })

    if (showBookingSheet) {
        ModalBottomSheet(
            onDismissRequest = { 
                showBookingSheet = false 
                bookingCompleted = false
            },
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle(color = GoldAccent.copy(alpha = 0.5f)) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bookingCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = GoldAccent,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Reservation Confirmed",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimaryDark,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your stay at ${currentProperty.name} has been successfully reserved and saved in your SQLite history.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryDark,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            showBookingSheet = false
                            bookingCompleted = false
                            onNavigateToBookings()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("VIEW BOOKINGS", color = Color.Black, style = MaterialTheme.typography.labelMedium)
                    }
                } else {
                    Text(
                        "Configure Sanctuary Stay",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp),
                        color = GoldAccent,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = currentProperty.name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimaryDark,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Dates Inputs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = checkInDate,
                            onValueChange = { checkInDate = it },
                            label = { Text("CHECK-IN DATE", color = TextSecondaryDark, fontSize = 10.sp) },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimaryDark),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldAccent,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        OutlinedTextField(
                            value = checkOutDate,
                            onValueChange = { checkOutDate = it },
                            label = { Text("CHECK-OUT DATE", color = TextSecondaryDark, fontSize = 10.sp) },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimaryDark),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldAccent,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Guests count
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("GUESTS", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                            Text("$guestsCount Traveler(s)", style = MaterialTheme.typography.bodyMedium, color = TextPrimaryDark)
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { if (guestsCount > 1) guestsCount-- },
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                            ) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", tint = GoldAccent)
                            }

                            Text("$guestsCount", color = TextPrimaryDark, fontWeight = FontWeight.Bold)

                            IconButton(
                                onClick = { guestsCount++ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = GoldAccent)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Final Cost Estimate
                    val nights = 7.0 // mock duration
                    val totalCost = currentProperty.pricePerNight * nights
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "7 Nights Stay Estimate",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryDark
                        )
                        Text(
                            "$${String.format("%,.0f", totalCost)}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = GoldAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.bookProperty(
                                propertyId = currentProperty.id,
                                propertyName = currentProperty.name,
                                propertyLocation = currentProperty.location,
                                propertyImageUrl = currentProperty.imageUrlList.firstOrNull() ?: "",
                                checkInDate = checkInDate,
                                checkOutDate = checkOutDate,
                                totalPrice = totalCost,
                                guestsCount = guestsCount
                            )
                            bookingCompleted = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "CONFIRM RESERVATION",
                            color = Color.Black,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Hero image carousel section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = images[page],
                            contentDescription = "${currentProperty.name} View $page",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Dark overlay at the bottom of carousel
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                                    )
                                )
                            )
                    )

                    // Overlay top bar navigations
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            IconButton(
                                onClick = { /* Share action */ },
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                                    .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "Share",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = { viewModel.toggleFavorite(currentProperty.id) },
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                                    .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (currentProperty.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (currentProperty.isFavorite) Color.Red else Color.White
                                )
                            }
                        }
                    }

                    // Carousel page indicators overlay
                    Row(
                        Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(images.size) { iteration ->
                            val color = if (pagerState.currentPage == iteration) GoldAccent else Color.White.copy(alpha = 0.3f)
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                }
            }

            // Specs section
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = currentProperty.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldAccent
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outline)
                        )
                        Text(
                            text = currentProperty.location.substringBefore(",").uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryDark
                        )
                    }

                    Text(
                        text = currentProperty.name,
                        style = MaterialTheme.typography.displayMedium.copy(fontSize = 32.sp),
                        color = TextPrimaryDark,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Horizontal specs breakdown (Bedrooms, Bathrooms, size)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        SpecIndicator(Icons.Outlined.Bed, "${currentProperty.beds} Bedrooms")
                        SpecIndicator(Icons.Outlined.Bathtub, "${currentProperty.baths} Bathrooms")
                        SpecIndicator(Icons.Outlined.Straighten, "${String.format("%,.0f", currentProperty.areaSqM.toDouble())} m²")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Starting at",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryDark
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "$${String.format("%,.0f", currentProperty.pricePerNight)}",
                            style = MaterialTheme.typography.displayMedium,
                            color = GoldAccent,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "/ night",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondaryDark,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                }
            }

            // Section: Curated Amenities Bento layout
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                    Text(
                        text = "Curated Amenities",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryDark,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        AmenityBentoCard(
                            title = "24/7 Personal Butler",
                            description = "Dedicated concierge service to manage every nuance of your stay, from unpacking to dinner reservations.",
                            icon = Icons.Outlined.RoomService
                        )

                        AmenityBentoCard(
                            title = "Private Michelin Chef",
                            description = "A bespoke culinary journey tailored to your preferences, featuring local organic ingredients and rare vintages.",
                            icon = Icons.Outlined.Restaurant
                        )

                        AmenityBentoCard(
                            title = "Secluded Beach Access",
                            description = "A private elevator carved into the cliff leads to a pristine, exclusive shoreline with full attendant service.",
                            icon = Icons.Outlined.BeachAccess
                        )
                    }
                }
            }

            // Description / Editorial copy
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
                    Text(
                        text = "The Experience",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryDark,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Italicized Quote section
                    Box(
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .border(BorderStroke(1.dp, GoldAccent.copy(alpha = 0.2f)), RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "\"An architectural masterpiece where the boundary between sky and sea dissolves. ${currentProperty.name} is more than a residence—it is a silent partner in your rejuvenation.\"",
                            style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic, fontSize = 16.sp),
                            color = TextPrimaryDark,
                            lineHeight = 24.sp
                        )
                    }

                    Text(
                        text = currentProperty.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryDark,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Small wrapping material tag chips
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        currentProperty.amenityList.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tag.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextPrimaryDark,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }
            }

            // Map preview section with dark coordinates mockup
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDjybwbRqQEefmsX9lSM_20BLLLgg6VuKO7Ybg-DJVip_IhuTHeMu3qfZdOofnVYN_y6qtZkxsWHAIt_6bRMR-a--0cq-o7-tjCzrHXjrWJ9C1Qsf4lOb-X_s4NdG3oiOAoDoLQxv0Xl7pkR_I78lNoUsILMTbBYZs3M0UZmqKP1Krt7eUcQ-uz1sYJhoibzYf8larSwldsgEU2qbO63TNkHGekzFAXM5rzJ-s9xW62-zr5iVOSaxOufeUn1fyLPWRV2GR5ALNowkOa",
                            contentDescription = "Map Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Dark vignette center
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                        )

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(GoldAccent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location Pin",
                                    tint = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                "EXCLUSIVE LOCATION DETAILS",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                            Text(
                                "Provided Upon Booking Confirmation",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                                color = TextSecondaryDark,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Floating Booking Bar at the very bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(32.dp)
                    ),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "TOTAL STAY EST. (7 NTS)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryDark,
                            fontSize = 8.sp
                        )
                        Text(
                            "$${String.format("%,.0f", currentProperty.pricePerNight * 7)}",
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                            color = GoldAccent
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { showBookingSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text(
                                "Book Now",
                                color = Color.Black,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        IconButton(
                            onClick = { showBookingSheet = true },
                            modifier = Modifier
                                .size(40.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Pick Dates",
                                tint = GoldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpecIndicator(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = TextSecondaryDark,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondaryDark
        )
    }
}

@Composable
fun AmenityBentoCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                RoundedCornerShape(4.dp)
            ),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = GoldAccent,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryDark,
                lineHeight = 22.sp
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}
