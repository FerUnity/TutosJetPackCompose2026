package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material.icons.outlined.Tune
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
import com.example.data.Property
import com.example.ui.AppViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: AppViewModel,
    onNavigateToDetails: (Int) -> Unit
) {
    val properties by viewModel.filteredProperties.collectAsState()
    val activeCategory by viewModel.selectedCategory.collectAsState()
    val activeBudgetMax by viewModel.selectedBudgetMax.collectAsState()

    var showBudgetDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Villas", "Estates", "Ski Chalets", "Islands")

    if (showBudgetDialog) {
        AlertDialog(
            onDismissRequest = { showBudgetDialog = false },
            title = {
                Text(
                    "Select Maximum Budget",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                    color = GoldAccent
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val budgetOptions = listOf(
                        null to "No Limit",
                        2000.0 to "$2,000 / night",
                        3000.0 to "$3,000 / night",
                        4000.0 to "$4,000 / night",
                        5000.0 to "$5,000 / night"
                    )

                    budgetOptions.forEach { (amount, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setBudgetMax(amount)
                                    showBudgetDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = activeBudgetMax == amount,
                                onClick = {
                                    viewModel.setBudgetMax(amount)
                                    showBudgetDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(label, color = TextPrimaryDark)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBudgetDialog = false }) {
                    Text("Close", color = GoldAccent)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp)
    ) {
        // Hero / Display title section
        item {
            Column(modifier = Modifier.padding(bottom = 24.dp)) {
                Text(
                    text = "Find your architectural",
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 32.sp),
                    color = TextPrimaryDark
                )
                Text(
                    text = "masterpiece.",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 32.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    ),
                    color = GoldAccent,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Horizontal filter chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Budget filter trigger
                item {
                    Button(
                        onClick = { showBudgetDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeBudgetMax != null) GoldAccent else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.border(
                            1.dp,
                            if (activeBudgetMax != null) GoldAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(20.dp)
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Tune,
                                contentDescription = "Budget Filter",
                                tint = if (activeBudgetMax != null) Color.Black else GoldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (activeBudgetMax != null) "MAX \$${activeBudgetMax!!.toInt()}" else "BUDGET",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (activeBudgetMax != null) Color.Black else GoldAccent
                            )
                        }
                    }
                }

                // Category chips
                items(categories) { category ->
                    val isSelected = activeCategory == category
                    Button(
                        onClick = { viewModel.setCategory(category) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) GoldAccent else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.border(
                            1.dp,
                            if (isSelected) GoldAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(20.dp)
                        )
                    ) {
                        Text(
                            text = category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) Color.Black else TextSecondaryDark
                        )
                    }
                }
            }
        }

        // Grid/List of Properties
        if (properties.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = "No properties found",
                        tint = TextSecondaryDark,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No sanctuaries found.",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                        color = TextPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Try adjusting your budget or category filters.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryDark
                    )
                }
            }
        } else {
            items(properties) { property ->
                PropertyCard(
                    property = property,
                    onClick = {
                        viewModel.selectProperty(property.id)
                        onNavigateToDetails(property.id)
                    },
                    onFavoriteToggle = {
                        viewModel.toggleFavorite(property.id)
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun PropertyCard(
    property: Property,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                AsyncImage(
                    model = property.imageUrlList.firstOrNull() ?: "",
                    contentDescription = property.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // High-fashion dark gradient vignette
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

                // Favorite overlay
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (property.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (property.isFavorite) Color.Red else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Category tag overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(GoldAccent, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = property.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                        fontSize = 9.sp
                    )
                }

                // Bottom Overlay title
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "FEATURED SANCTUARY",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldAccent,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = property.name,
                                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                                color = Color.White
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$${property.pricePerNight.toInt()}",
                                style = MaterialTheme.typography.displayMedium.copy(fontSize = 22.sp),
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "/night",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryDark,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Specs row beneath the image
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location",
                        tint = TextSecondaryDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = property.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryDark
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Bed,
                            contentDescription = "Beds",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${property.beds} BEDS",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Pool,
                            contentDescription = "Baths",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${property.baths} BATHS",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}
