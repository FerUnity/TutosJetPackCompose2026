package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.AppViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    onNavigateToDetails: (Int) -> Unit
) {
    val bookings by viewModel.allBookings.collectAsState()
    val properties by viewModel.allProperties.collectAsState()

    // Get favorite properties
    val favorites = properties.filter { it.isFavorite }

    // Fallbacks for saved estates if user has none, to keep the Bento Grid visually populated like the mockup
    val bentoEstates = listOf(
        BentoItem(
            name = "Azure Horizon Villa",
            location = "Amalfi, Italy",
            tag = "MOST VIEWED",
            image = "https://lh3.googleusercontent.com/aida-public/AB6AXuCqa_8i9SqdQ_pYw3mWzMMA8GEvJahXhaRugI8IH29bU5b8MKACg0yVWY1Aqjk4gKIptFQX3iobVWB-XOC1CWemGmBnLC1IhBaGUO9dKxRSuXojLd3iYYssErzMAvXj1VWGDpq8exME2uW4jtP5y4AYxpC8fDy0qEdKDwhgV-VkgRNNkB63RRjHN-ixiCrNR3dGok4HOraJ7Z6whaKTaCs9wEZ7yIZr6nwHiQXtd0_HPYpkoCT31VVdmMf2GzSOzjD-BgKDiKdr94e8"
        ),
        BentoItem(
            name = "Zen Sanctuary",
            location = "Kyoto, Japan",
            tag = "ZEN",
            image = "https://lh3.googleusercontent.com/aida-public/AB6AXuA4p9EQ8DaZqWWFTA2TUTjh18KcIjtuppZgj-rgFnFCEQKkDl5TacyCCb7q81Ypj6CCBvT7zSmshHh5WA7ZbOPt_F8_4BxgaZfhg5BDQ_OW-KJ5DvGRAlqafmDAMA_djpiiu_Vp9Mq3Eu29uzQ_IgLKmkQ2QJn9YrxCsm4Y3c8htcBFy1Lmb3Px6YRLeKJ5BzoBdF0MWw-7EnWV0ikoqOIWv4H-HD5ZRnM8BdQtGQYdLbkPrIrV9i4Qxf9YKLBgjQG6s6yOV-9rzhGr"
        ),
        BentoItem(
            name = "Dune Observance",
            location = "Sossusvlei, Namibia",
            tag = "MILKY WAY",
            image = "https://lh3.googleusercontent.com/aida-public/AB6AXuCeiSOqZbhg2iSRdLBvs56fHcmqV6vnfLvdO_xr0iJnxCNrkQLxJ5qh-5AndDz70zVpk6U3kAA4frkC__LxANsDeiIQ-fxZnRmysDl7JRW8x7YA3TReFXRRIkw4GBJGB8Lc4omhx1CXB8V8vDuzUx3XI2V44v1z13DZWN7hAM1UTjdt5xRs15QiY8zkNmebX3EzFaNQrl3LZdg5YJI0vg54kdLCJKv3CbVBYSTFxuPWK_-Rhp3Apy89Ufg63pEWWgDRu_Fn6nadGgTT"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp)
    ) {
        // Upper profile bar (Julian with profile image and level status)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Welcome back, Julian",
                        style = MaterialTheme.typography.displayMedium.copy(fontSize = 32.sp),
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "PLATINUM CONCIERGE TIER MEMBER",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldAccent,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Profile picture matching portrait mockup exactly
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCq8q79LF1yPXbtZmxy3ckhCX70IAJ6mUzrj8ktiTbdy0cF8BThQpm379Y5vi1hX3JPdCcrCEDwUGUsar_Dv7z7m20aCDx7FUpcHovptGJrRZ_9-OL2QiRbbd5RAQvjJYrcPi0_F_3VJ0LZ2I-dRhoKec9N2_k4oflkH2il-4hNjo8JNyvQDiCCOdlsdw1lA_69hFVUtyJNYEJQtdDNMYvWYCYUT-DyKXgPtWx4i9vRo78AxF7N-wn2C6Y9nrcK5Ui3dAfvz4t0leec",
                    contentDescription = "Julian's Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(1.dp, GoldAccent.copy(alpha = 0.5f), CircleShape)
                )
            }
        }

        // Travel stats grid (Total stays, Travel credits, Elite points)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total stays
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                            RoundedCornerShape(8.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            "TOTAL STAYS",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldAccent,
                            fontSize = 9.sp
                        )
                        Text(
                            "14",
                            style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                            color = TextPrimaryDark,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Travel credits
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                            RoundedCornerShape(8.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            "TRAVEL CREDITS",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldAccent,
                            fontSize = 9.sp
                        )
                        Text(
                            "$1,240",
                            style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                            color = TextPrimaryDark,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Elite points
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                            RoundedCornerShape(8.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            "ELITE POINTS",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldAccent,
                            fontSize = 9.sp
                        )
                        Text(
                            "8.2k",
                            style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                            color = TextPrimaryDark,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        // Section: Upcoming Stays
        item {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Upcoming Stays",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "VIEW HISTORY",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldAccent,
                        modifier = Modifier.clickable { /* History action */ }
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(GoldAccent)
                        .padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Render dynamic bookings stay card or placeholder card
                val activeStay = bookings.firstOrNull()
                if (activeStay != null) {
                    UpcomingStayCard(
                        bookingName = activeStay.propertyName,
                        bookingLocation = activeStay.propertyLocation,
                        imageUrl = activeStay.propertyImageUrl,
                        checkIn = activeStay.checkInDate,
                        checkOut = activeStay.checkOutDate,
                        daysLeft = "In 12 Days", // customized
                        onManageBooking = { /* Action */ }
                    )
                } else {
                    // Fallback Obsidian Chalet stay matching mockup
                    UpcomingStayCard(
                        bookingName = "The Obsidian Chalet",
                        bookingLocation = "Zermatt, Switzerland",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCVrTxQkF8sIRr5irJeu83mnMCtCsj_1iKxRAdz0J-RvK-eC7szjG542qZc90lT6TrwuAuAJBpBZ6hDaEav3t3SfmQY_krPYZiA4Qvtw8aY-QyWr_I_jdNNmTl-ModQpH0vZrZeKC-PMGmTsc9sVTS-fPbrymZxoioO1C_oQo7bSS1OA9JoEz8q_3Q9kv8j88CZxzfHeDmd05gTCNCSUNAuRq3cX9rsWUzZoOtxFHhCgv-MYEeL1fV53DlWUUfQ5deQYin07QKWlzD2",
                        checkIn = "Dec 14, 2024",
                        checkOut = "Dec 21, 2024",
                        daysLeft = "In 12 Days",
                        onManageBooking = {}
                    )
                }
            }
        }

        // Section: Saved Estates Bento Grid
        item {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Saved Estates",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "SEE ALL (${favorites.size.coerceAtLeast(24)})",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldAccent
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(GoldAccent)
                        .padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bento Grid Layout (Large item occupying full width + 2 items next to each other or beautifully styled)
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Item 1 (Large full card)
                    val mainItem = favorites.firstOrNull()?.let {
                        BentoItem(it.name, it.location, "SAVED", it.imageUrlList.firstOrNull() ?: bentoEstates[0].image)
                    } ?: bentoEstates[0]

                    BentoCardLarge(
                        item = mainItem,
                        onClick = {
                            val id = favorites.firstOrNull()?.id ?: 8
                            onNavigateToDetails(id)
                        }
                    )

                    // Secondary items side-by-side
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        BentoCardSmall(
                            item = bentoEstates[1],
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToDetails(6) }
                        )
                        BentoCardSmall(
                            item = bentoEstates[2],
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToDetails(7) }
                        )
                    }
                }
            }
        }

        // Section: Account Settings Links
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingsLinkRow("Payment Methods & Billing", Icons.Outlined.Payments)
                SettingsLinkRow("Privacy & Security Settings", Icons.Outlined.Security)
                SettingsLinkRow("24/7 Concierge Support", Icons.Outlined.SupportAgent)
                SettingsLinkRow("Sign Out", Icons.Outlined.Logout, isDestructive = true)
            }
        }
    }
}

data class BentoItem(
    val name: String,
    val location: String,
    val tag: String,
    val image: String
)

@Composable
fun UpcomingStayCard(
    bookingName: String,
    bookingLocation: String,
    imageUrl: String,
    checkIn: String,
    checkOut: String,
    daysLeft: String,
    onManageBooking: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = bookingName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        daysLeft.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextPrimaryDark,
                        fontSize = 10.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        bookingName,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                        color = TextPrimaryDark
                    )
                    Icon(
                        imageVector = Icons.Filled.VerifiedUser,
                        contentDescription = "Verified Stay",
                        tint = GoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    bookingLocation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryDark,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "CHECK-IN",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryDark,
                            fontSize = 9.sp
                        )
                        Text(
                            checkIn,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                            color = TextPrimaryDark,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Column {
                        Text(
                            "CHECK-OUT",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryDark,
                            fontSize = 9.sp
                        )
                        Text(
                            checkOut,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                            color = TextPrimaryDark,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.VpnKey,
                            contentDescription = "Digital Key",
                            tint = TextSecondaryDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "DIGITAL KEY ACTIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimaryDark,
                            fontSize = 10.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onManageBooking,
                        border = BorderStroke(1.dp, GoldAccent),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "MANAGE BOOKING",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BentoCardLarge(
    item: BentoItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = item.image,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(GoldAccent, RoundedCornerShape(2.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    item.tag.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    fontSize = 8.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                item.name,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                color = Color.White
            )
            Text(
                item.location,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Saved",
                tint = Color.Red,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun BentoCardSmall(
    item: BentoItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(130.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = item.image,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                item.name.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontSize = 10.sp
            )
            Text(
                item.location,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 9.sp
            )
        }
    }
}

@Composable
fun SettingsLinkRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDestructive: Boolean = false
) {
    val tint = if (isDestructive) MaterialTheme.colorScheme.error else GoldAccent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Link Row Action */ }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDestructive) MaterialTheme.colorScheme.error else TextPrimaryDark
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = TextSecondaryDark,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
