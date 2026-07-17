package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Property
import com.example.ui.AppViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDim
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToExplore: () -> Unit
) {
    val properties by viewModel.allProperties.collectAsState()
    var searchInput by remember { mutableStateOf("") }

    val categories = listOf(
        "Villas" to "Minimalist Mediterranean and coastal sanctuaries.",
        "Mansions" to "Grand historic and noble European estates.",
        "Penthouse" to "Ultra-modern city high-rises with majestic views.",
        "Ski Chalets" to "Luxurious modern log cabins in snowy forests."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Hero Section with Background image
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
            ) {
                // Background image with luxury mountains
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAAlq-2pRCZIdHoAKPveEv7hZQWYExsdZYVTOcNiz_V8hrHNvf3P9NpNnDgWhMhrwg5zK_eh-viB9rIe23Z9W7z68dY8a1wbGLrnZmhsWb6D8FUKMR_jFq-4rg0b4ng8uf_dsxsuWvvxe60DtsAlN41-00P8bQhiJ_dO7wdCOMQ9xjny0DYQeeQzbj-3pvx1QxawrycTtrTciFqDXcWKn12Sl6mZRqGa496AXna4iQrad1IliHeKp1B4O1yr_BUkMfEdmNE3o_xKgkG",
                    contentDescription = "Swiss Alps Sanctuary",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Atmospheric Luxury Dark Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )

                // Search Overlay content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Find your quiet place.",
                        style = MaterialTheme.typography.displayMedium,
                        color = TextPrimaryDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Curated sanctuaries for the discerning traveler.",
                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                        color = TextSecondaryDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Refined search bar card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                RoundedCornerShape(32.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                        ),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Explore,
                                contentDescription = "Explore",
                                tint = GoldAccent,
                                modifier = Modifier.size(22.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            TextField(
                                value = searchInput,
                                onValueChange = {
                                    searchInput = it
                                    viewModel.setSearchQuery(it)
                                },
                                placeholder = {
                                    Text(
                                        "Where do you wish to go?",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark.copy(alpha = 0.6f))
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimaryDark),
                                modifier = Modifier.weight(1f)
                            )

                            // Quick Explore CTA
                            IconButton(
                                onClick = {
                                    if (searchInput.isNotEmpty()) {
                                        onNavigateToExplore()
                                    } else {
                                        onNavigateToExplore()
                                    }
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(GoldAccent, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 1: Curations (Architectural Styles)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 16.dp)) {
                    Text(
                        text = "CURATIONS",
                        style = MaterialTheme.typography.labelMedium,
                        color = GoldAccent,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Architectural Styles",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimaryDark
                    )
                }

                // Horizontal list of styles
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories) { (style, subtitle) ->
                        val sampleImg = when (style) {
                            "Villas" -> "https://lh3.googleusercontent.com/aida-public/AB6AXuAd1X4Vqox2SZOMFq5sJ0w7g00cSpCVPQZZO_wHnqHz_v5lkQ8yT36v36jpp19-Bw-zQzu6TpjuTyi6JFY-jFsCP67LAusqM-6n9L546DMUlnMgDTSR1VRMNdnquVtyNmGHx-2DqPaq4h-UMxrG3IwOODSQFkJQlS6gq8mTFY2urgxfVSb-ttOQNEqpMe7q5l0kV6iQVVBusxiHrCJr9TDBBbTAs-aoJp4J-HENhwNoo7VNLLyi0R1T30Pg7-VcQSS_u0HQaewq5TXh"
                            "Mansions" -> "https://lh3.googleusercontent.com/aida-public/AB6AXuC2beuR3zphOSaObzk9c_6-EoyKmEbKPE5eOkfuACjyANO2gvkw2fDpVRPhEFsH9rb86apMMHRnVdOURE8W8pqYhynUc-hQF6hqroG7xsrjPYZCGMuk9_JoOw6qGx2OO6nmSASFe5JTlKSc_WQs-wCEyuNrHJo44NWh4bJxPztgt1HoJwKB-J-SvathyWz2ADhsVrrM564gbSXS-775-2hb1Q-8MmhqolFEDZRyKn76rCLHqqwie1-BtewiBQ7CInOb6wdWSj7NYrK0"
                            "Penthouse" -> "https://lh3.googleusercontent.com/aida-public/AB6AXuBwkAndvjFDl_zkOdbEzP1kGf8iPc-FFpMZVaodxITRq_3b42skAzaYx4BuQJjcXrQlkXhf4-AI8EZnC_WoLPe1VDmR1I17eRfVUWLkfaCnt0A0o-OqidWSIrcpJ52tqrdxa7l23ByUYe68RcqzghxrHQx_vABC3e25Jrkl8eAUIXfwq5jcfKczXR37jtrLE2mFsKy6x8N6ANRJ5wpkAvhsub5yWuZMJgipHg4Vt1xvBcFTKTIR0Dlw3nY5-5dgcoYzJH1Nv5BdCq83"
                            else -> "https://lh3.googleusercontent.com/aida-public/AB6AXuBCf4J8RsO91q2mLlOt6J5mKBvexYU6PbdZtne3fC5pGchCvA-zugEnaK1T-Yc0Dm5fE4hQuko1S8SeH9j8zBFGnSURXhFXCVy3LMCCYx9sdAiypi16dozecUDV3CujIRKuoEFp3uZ0cUZn6D16IyX0fcDegcCjT65TA7VbiTSVApP3ay8pS0GMobliqDngTOUcxcPs48waqVKFAhhwZ2eaC_T-XoTlEh7Ug93NB6fKOmBDSpXypdj29V_bRp1lvmNH9jkgRP_iunfQ"
                        }

                        Column(
                            modifier = Modifier
                                .width(180.dp)
                                .clickable {
                                    viewModel.setCategory(style)
                                    onNavigateToExplore()
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(180.dp)
                                    .height(230.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                AsyncImage(
                                    model = sampleImg,
                                    contentDescription = style,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.2f))
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = style.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = GoldAccent,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Exclusives (Featured Properties)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp).padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "EXCLUSIVES",
                        style = MaterialTheme.typography.labelMedium,
                        color = GoldAccent,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Featured Destinations",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimaryDark,
                        textAlign = TextAlign.Center
                    )
                }

                // Show top 3 properties with beautiful design
                val featuredList = properties.take(3)
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    featuredList.forEach { property ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(380.dp)
                                .clickable {
                                    viewModel.selectProperty(property.id)
                                    onNavigateToDetails(property.id)
                                },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = property.imageUrlList.firstOrNull() ?: "",
                                    contentDescription = property.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Dark overlay gradient at the bottom
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

                                // Favorite Button overlay
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(16.dp)
                                ) {
                                    IconButton(
                                        onClick = { viewModel.toggleFavorite(property.id) },
                                        modifier = Modifier
                                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                                            .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = if (property.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                            contentDescription = "Favorite",
                                            tint = if (property.isFavorite) Color.Red else Color.White
                                        )
                                    }
                                }

                                // Bottom Label details
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(24.dp)
                                ) {
                                    Text(
                                        text = property.location.substringBefore(",").uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldAccent
                                    )
                                    Text(
                                        text = property.name,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Private Concierge Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(32.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Private Concierge",
                        style = MaterialTheme.typography.displayMedium.copy(fontSize = 28.sp),
                        color = TextPrimaryDark,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = "Access our most exclusive, off-market properties and bespoke travel arrangements through our dedicated sanctuary consultants.",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        color = TextSecondaryDark,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { /* Apply Access action */ },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "APPLY FOR ACCESS",
                                style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp),
                                color = Color.Black
                            )
                        }

                        OutlinedButton(
                            onClick = { /* Mission action */ },
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "OUR MISSION",
                                style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp),
                                color = GoldAccent
                            )
                        }
                    }
                }
            }
        }
    }
}
