package com.example.ui.screens

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.data.database.CallRecordEntity
import com.example.data.model.Contact
import com.example.ui.theme.*
import com.example.ui.viewmodel.ContactViewModel
import com.example.ui.viewmodel.NavigationTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsListScreen(
    viewModel: ContactViewModel
) {
    val context = LocalContext.current
    val contacts by viewModel.uiContactsState.collectAsState()
    val callHistory by viewModel.callRecords.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Runtime Permission Launcher for reading contacts
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updatePermissionStatus(isGranted)
    }

    // Run permission check on display
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("contacts_list_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (viewModel.currentTab) {
                            NavigationTab.Contacts -> "Contactos"
                            NavigationTab.Recents -> "Llamadas Recientes"
                            NavigationTab.Favorites -> "Favoritos"
                            NavigationTab.Settings -> "Configuración"
                        },
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif,
                            color = SaharaSienna
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Sidebar / menu action */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = SaharaSienna
                        )
                    }
                },
                actions = {
                    // Avatar profile placeholder conforming to luxury brand styling
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.dp, SaharaOutline.copy(alpha = 0.5f), CircleShape)
                            .background(SaharaCard)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCp_2OaDMqW3vqbezScXgJZDEEVASK8wdjY15ikUiSpfIcWUc2vSPJYtRWVAciP_7-Mn0y2LSvst9FhheN7lqXWPHb17u75PzkciCFSPGC8W4Vf_gF4RGdFxw86y0lJ1ccuhzaxQGiB-hs13g2sYVQgA10G43ho4GqxXvMbngoJypfYWRBHI_yOA3j7SWuhqL7zlinq-Bs_zmCUSz_Q71T55DnhiaFbOzXbi3Dcq3t_o9vj8UB9rq-quZZAdAm-aaKuYL_3sb5AOmQ"
                            ),
                            contentDescription = "Perfil del usuario",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaharaLinen
                )
            )
        },
        floatingActionButton = {
            if (viewModel.currentTab == NavigationTab.Contacts) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = SaharaSienna,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("add_contact_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar contacto",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                activeTab = viewModel.currentTab,
                onTabSelected = { viewModel.currentTab = it }
            )
        },
        containerColor = SaharaLinen
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (viewModel.currentTab) {
                NavigationTab.Contacts, NavigationTab.Favorites -> {
                    // Search bar for searching contacts
                    SearchBarSection(
                        query = viewModel.searchQuery,
                        onQueryChanged = { viewModel.searchQuery = it }
                    )

                    if (contacts.isEmpty()) {
                        EmptyContactsState(
                            isFavoritesOnly = viewModel.currentTab == NavigationTab.Favorites
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("contacts_lazy_column"),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(contacts, key = { it.id }) { contact ->
                                ContactCard(
                                    contact = contact,
                                    onCallClicked = { viewModel.startCall(contact) },
                                    onEmailClicked = { viewModel.sendEmail(context, contact.name) },
                                    onLocationClicked = { viewModel.openInGoogleMaps(context, contact.address) },
                                    onToggleFavorite = { viewModel.toggleFavorite(contact) },
                                    onDeleteContact = { viewModel.deleteContact(contact) }
                                )
                            }
                        }
                    }
                }
                NavigationTab.Recents -> {
                    RecentsTabContent(
                        callHistory = callHistory,
                        onClearAll = { viewModel.clearCallHistory() },
                        onCallClicked = { record ->
                            // Map record to temporary contact for dialer
                            val matchedContact = contacts.find { it.phone == record.phone }
                                ?: Contact(
                                    id = record.contactId,
                                    name = record.name,
                                    phone = record.phone,
                                    address = "",
                                    company = "",
                                    photoUri = record.photoUri,
                                    isFavorite = false,
                                    isSystemContact = false
                                )
                            viewModel.startCall(matchedContact)
                        }
                    )
                }
                NavigationTab.Settings -> {
                    SettingsTabContent(
                        isGranted = viewModel.isContactsPermissionGranted,
                        onRequestPermission = { permissionLauncher.launch(Manifest.permission.READ_CONTACTS) },
                        onInjectMock = {
                            viewModel.addNewContact(
                                name = "Mateo Rivera",
                                phone = "+34 699 000 111",
                                address = "Paseo del Sol 12, Marbella",
                                company = "Rivera Design Studio",
                                photoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAUfL__MozBh1gS51qyeca2KRzBLOzWGkKlYjGgEZprxV6InWM48Mq6KQ2K-6Op69aUXMziXfUSFUGuBbGljr-29sy_mcmeSllLXvBcZnXcxu48yNvMEiqQQe7wmFPMbC6LhNDWOB6Mz5jJlbsFtzZ0ZeNPIrxkPHTwnx-t7ckfVrY3Pz_po-EjtvXlvhkgkR4GIogguRvuX7lUFB1mqf8Ja6Jpw6flc-jkpnV2GX5pyUS8XX8WbjQmlEd11Pv76JCaWMJftTYKUHQ",
                                isFavorite = false
                            )
                        },
                        onClearCalls = { viewModel.clearCallHistory() }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddContactDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, phone, address, company, photoUrl, isFav ->
                viewModel.addNewContact(name, phone, address, company, photoUrl, isFav)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun SearchBarSection(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            placeholder = {
                Text(
                    text = "Buscar contactos...",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SaharaGray)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = SaharaGray
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar",
                            tint = SaharaGray
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = SaharaCharcoal,
                unfocusedTextColor = SaharaCharcoal,
                focusedContainerColor = SaharaWhite,
                unfocusedContainerColor = SaharaCard,
                focusedBorderColor = SaharaSienna,
                unfocusedBorderColor = SaharaOutline.copy(alpha = 0.5f),
                cursorColor = SaharaSienna
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_bar")
        )
    }
}

@Composable
fun ContactCard(
    contact: Contact,
    onCallClicked: () -> Unit,
    onEmailClicked: () -> Unit,
    onLocationClicked: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDeleteContact: () -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("contact_card_${contact.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SaharaCard
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        border = BorderStroke(1.dp, SaharaOutline.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Elegant top cover photo of the contact (if available) or initials cover
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(SaharaCardHigh)
            ) {
                if (contact.photoUri.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = contact.photoUri),
                        contentDescription = "Foto de ${contact.name}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // High-fashion abstract initial logo
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    listOf(SaharaCardHigh, SaharaOutline.copy(alpha = 0.3f))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = contact.name.take(1).uppercase(),
                            style = MaterialTheme.typography.displayLarge.copy(
                                color = SaharaSienna.copy(alpha = 0.4f),
                                fontWeight = FontWeight.Light
                            )
                        )
                    }
                }

                // If contact is favorite, show favorite badge
                if (contact.isFavorite) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.TopEnd)
                            .background(SaharaRose, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "FAVORITO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }

            // Core contact details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Name Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = SaharaCharcoal,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Row {
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (contact.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Alternar favorito",
                                tint = if (contact.isFavorite) SaharaRose else SaharaGray
                            )
                        }

                        if (!contact.isSystemContact) {
                            Box {
                                IconButton(onClick = { expandedMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Más opciones",
                                        tint = SaharaGray
                                    )
                                }
                                DropdownMenu(
                                    expanded = expandedMenu,
                                    onDismissRequest = { expandedMenu = false },
                                    modifier = Modifier.background(SaharaWhite)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Eliminar Contacto", color = SaharaError) },
                                        onClick = {
                                            expandedMenu = false
                                            onDeleteContact()
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = SaharaError
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Detail Lines: Address, Phone, Company
                if (contact.address.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onLocationClicked() }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Dirección",
                            tint = SaharaSienna,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = contact.address,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SaharaCharcoal,
                                fontWeight = FontWeight.Normal
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (contact.phone.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Teléfono",
                            tint = SaharaSienna,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = contact.phone,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SaharaCharcoal,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }

                if (contact.company.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Compañía",
                            tint = SaharaSienna,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = contact.company,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SaharaGray,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action row conformant to specifications: Wide primary CTA & mail envelope
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onCallClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SaharaSienna,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("call_button_${contact.id}")
                    ) {
                        Text(
                            text = "LLAMAR",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                fontSize = 12.sp
                            )
                        )
                    }

                    OutlinedButton(
                        onClick = onEmailClicked,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, SaharaOutline.copy(alpha = 0.8f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SaharaSienna
                        ),
                        modifier = Modifier
                            .size(44.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mail,
                            contentDescription = "Enviar correo",
                            tint = SaharaSienna,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyContactsState(isFavoritesOnly: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isFavoritesOnly) Icons.Default.StarOutline else Icons.Default.Contacts,
            contentDescription = null,
            tint = SaharaOutline,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isFavoritesOnly) "No tienes contactos favoritos guardados" else "No se encontraron contactos",
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily.Serif,
                color = SaharaCharcoal
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isFavoritesOnly) "Presiona la estrella en tus contactos para agregarlos aquí." else "Usa el botón '+' para agregar un contacto o importa desde tu teléfono.",
            style = MaterialTheme.typography.bodyMedium.copy(color = SaharaGray),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RecentsTabContent(
    callHistory: List<CallRecordEntity>,
    onClearAll: () -> Unit,
    onCallClicked: (CallRecordEntity) -> Unit
) {
    if (callHistory.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = SaharaOutline,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Historial vacío",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    color = SaharaCharcoal
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tus llamadas simuladas aparecerán aquí.",
                style = MaterialTheme.typography.bodyMedium.copy(color = SaharaGray)
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Llamadas Recientes",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SaharaCharcoal
                    )
                )
                TextButton(onClick = onClearAll) {
                    Text(
                        text = "Limpiar Todo",
                        style = MaterialTheme.typography.labelLarge.copy(color = SaharaRose)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(callHistory) { record ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCallClicked(record) },
                        colors = CardDefaults.cardColors(containerColor = SaharaCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Avatar profile circular
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(SaharaCardHigh)
                                ) {
                                    if (record.photoUri.isNotBlank()) {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = record.photoUri),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = SaharaGray
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = record.name,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CallMade,
                                            contentDescription = "Llamada Saliente",
                                            tint = SaharaSienna,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        val minutes = record.durationSeconds / 60
                                        val seconds = record.durationSeconds % 60
                                        Text(
                                            text = "Duración: %02d:%02d".format(minutes, seconds),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = SaharaGray,
                                                fontSize = 12.sp
                                            )
                                        )
                                    }
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                val sdf = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
                                Text(
                                    text = sdf.format(Date(record.timestamp)),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = SaharaGray,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Llamar de nuevo",
                                    tint = SaharaSienna,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsTabContent(
    isGranted: Boolean,
    onRequestPermission: () -> Unit,
    onInjectMock: () -> Unit,
    onClearCalls: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Acciones y Permisos",
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily.Serif,
                color = SaharaCharcoal
            )
        )

        HorizontalDivider(color = SaharaOutline.copy(alpha = 0.5f))

        // Permission Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SaharaCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Acceso a Contactos",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isGranted) "Concedido. Los contactos de tu teléfono se están integrando con Sahara Connect." else "No se ha concedido el acceso. Solo verás los contactos locales de Sahara.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SaharaCharcoal)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onRequestPermission,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isGranted) SaharaGray else SaharaSienna
                    )
                ) {
                    Text(text = if (isGranted) "Permiso Concedido" else "Conceder Permiso")
                }
            }
        }

        // Action Options Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SaharaCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Operaciones de Datos",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onInjectMock,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, SaharaOutline)
                ) {
                    Text(text = "Re-inyectar Mateo Rivera", color = SaharaSienna)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onClearCalls,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, SaharaOutline)
                ) {
                    Text(text = "Limpiar Historial de Llamadas", color = SaharaRose)
                }
            }
        }
    }
}

@Composable
fun AddContactDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, address: String, company: String, photoUrl: String, isFavorite: Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SaharaLinen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            border = BorderStroke(1.dp, SaharaOutline)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Agregar Contacto",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Serif,
                        color = SaharaSienna
                    )
                )

                HorizontalDivider(color = SaharaOutline.copy(alpha = 0.5f))

                // Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                // Phone Input
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Número de Teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                // Address Input
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección (Abrir en Maps)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                // Company Input
                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Compañía / Estudio") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                // Photo URL Input
                OutlinedTextField(
                    value = photoUrl,
                    onValueChange = { photoUrl = it },
                    label = { Text("Foto URL (Opcional)") },
                    placeholder = { Text("https://...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                // Favorite Row Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Marcar como Favorito",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Switch(
                        checked = isFavorite,
                        onCheckedChange = { isFavorite = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = SaharaSienna,
                            uncheckedThumbColor = SaharaGray,
                            uncheckedTrackColor = SaharaCard
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancelar", color = SaharaGray)
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank() && phone.isNotBlank()) {
                                onConfirm(name, phone, address, company, photoUrl, isFavorite)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaharaSienna),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        enabled = name.isNotBlank() && phone.isNotBlank()
                    ) {
                        Text(text = "Guardar", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    activeTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    NavigationBar(
        containerColor = SaharaCard,
        tonalElevation = 0.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        NavigationBarItem(
            selected = activeTab == NavigationTab.Contacts,
            onClick = { onTabSelected(NavigationTab.Contacts) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Contacts,
                    contentDescription = "Contactos"
                )
            },
            label = { Text("CONTACTOS", fontSize = 9.sp, letterSpacing = 1.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SaharaSienna,
                selectedTextColor = SaharaSienna,
                indicatorColor = SaharaSiennaLight,
                unselectedIconColor = SaharaGray,
                unselectedTextColor = SaharaGray
            )
        )

        NavigationBarItem(
            selected = activeTab == NavigationTab.Recents,
            onClick = { onTabSelected(NavigationTab.Recents) },
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Recientes"
                )
            },
            label = { Text("RECIENTES", fontSize = 9.sp, letterSpacing = 1.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SaharaSienna,
                selectedTextColor = SaharaSienna,
                indicatorColor = SaharaSiennaLight,
                unselectedIconColor = SaharaGray,
                unselectedTextColor = SaharaGray
            )
        )

        NavigationBarItem(
            selected = activeTab == NavigationTab.Favorites,
            onClick = { onTabSelected(NavigationTab.Favorites) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favoritos"
                )
            },
            label = { Text("FAVORITOS", fontSize = 9.sp, letterSpacing = 1.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SaharaSienna,
                selectedTextColor = SaharaSienna,
                indicatorColor = SaharaSiennaLight,
                unselectedIconColor = SaharaGray,
                unselectedTextColor = SaharaGray
            )
        )

        NavigationBarItem(
            selected = activeTab == NavigationTab.Settings,
            onClick = { onTabSelected(NavigationTab.Settings) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración"
                )
            },
            label = { Text("AJUSTES", fontSize = 9.sp, letterSpacing = 1.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SaharaSienna,
                selectedTextColor = SaharaSienna,
                indicatorColor = SaharaSiennaLight,
                unselectedIconColor = SaharaGray,
                unselectedTextColor = SaharaGray
            )
        )
    }
}
