package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.CallRecordEntity
import com.example.data.database.ContactDatabase
import com.example.data.model.Contact
import com.example.data.repository.ContactRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class Screen {
    Welcome,
    ContactsList,
    ActiveCall,
    CallEnded,
    CallFailed
}

enum class NavigationTab {
    Contacts,
    Recents,
    Favorites,
    Settings
}

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ContactDatabase.getDatabase(application)
    private val repository = ContactRepository(application, database.contactDao())

    // Permissions & Setup State
    var isContactsPermissionGranted by mutableStateOf(false)
        private set

    // Navigation & UI state
    var currentScreen by mutableStateOf(Screen.Welcome)
    var currentTab by mutableStateOf(NavigationTab.Contacts)
    var searchQuery by mutableStateOf("")

    // Active Call States
    var activeCallContact by mutableStateOf<Contact?>(null)
        private set
    var activeCallDuration by mutableStateOf(0)
        private set
    var activeCallState by mutableStateOf("Llamando...")
        private set
    private var timerJob: Job? = null

    // Fetch contacts from repo
    private val _hasPermissionFlow = MutableStateFlow(false)
    
    val allContacts: StateFlow<List<Contact>> = _hasPermissionFlow
        .flatMapLatest { hasPermission ->
            repository.getMergedContactsFlow(hasPermission)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Filtered contacts based on query and tabs
    val uiContactsState: StateFlow<List<Contact>> = combine(
        allContacts,
        snapshotFlow { searchQuery },
        snapshotFlow { currentTab }
    ) { contacts, query, tab ->
        var filtered = contacts
        
        // Filter by tab
        if (tab == NavigationTab.Favorites) {
            filtered = filtered.filter { it.isFavorite }
        }

        // Filter by search query
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.phone.contains(query, ignoreCase = true) ||
                it.company.contains(query, ignoreCase = true) ||
                it.address.contains(query, ignoreCase = true)
            }
        }
        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Call history
    val callRecords: StateFlow<List<CallRecordEntity>> = repository.callRecordsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            // Pre-populate default curated contacts if database is empty
            repository.initializeDefaultContactsIfEmpty()
        }
    }

    fun updatePermissionStatus(granted: Boolean) {
        isContactsPermissionGranted = granted
        _hasPermissionFlow.value = granted
    }

    // Call Operations
    fun startCall(contact: Contact) {
        activeCallContact = contact
        activeCallDuration = 0
        activeCallState = "Llamando..."
        currentScreen = Screen.ActiveCall

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            // First delay to simulate call ring/connection
            delay(2500)
            if (contact.phone.replace(" ", "").isEmpty()) {
                // If phone is empty, fail the call!
                currentScreen = Screen.CallFailed
                return@launch
            }
            
            activeCallState = "Conectado"
            while (isActive) {
                delay(1000)
                activeCallDuration++
            }
        }
    }

    fun endCall() {
        timerJob?.cancel()
        val duration = activeCallDuration
        val contact = activeCallContact
        currentScreen = Screen.CallEnded

        if (contact != null) {
            viewModelScope.launch {
                repository.addCallRecord(
                    CallRecordEntity(
                        contactId = contact.id,
                        name = contact.name,
                        phone = contact.phone,
                        photoUri = contact.photoUri,
                        timestamp = System.currentTimeMillis(),
                        durationSeconds = duration,
                        isOutgoing = true
                    )
                )
            }
        }
    }

    fun retryCall() {
        val contact = activeCallContact
        if (contact != null) {
            startCall(contact)
        } else {
            currentScreen = Screen.ContactsList
        }
    }

    fun clearCallHistory() {
        viewModelScope.launch {
            repository.clearCallRecords()
        }
    }

    // Contact Operations
    fun toggleFavorite(contact: Contact) {
        viewModelScope.launch {
            repository.toggleFavorite(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    fun addNewContact(name: String, phone: String, address: String, company: String, photoUrl: String, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.addLocalContact(
                name = name,
                phone = phone,
                address = address,
                company = company,
                photoUri = if (photoUrl.isBlank()) "https://lh3.googleusercontent.com/aida-public/AB6AXuBd1YGjWAIqTPDy9aqv8lwdY1egtKPGZngt5Pcl4t3sQQvApR3rcKW9ZPAhX50gNKZaTU-cSl3s4ZeIUtTC7fZiNKvO9P6WTKWsT4FVWW6ZEt_l88QsjjpVEFqgchbpvhmB9ErzQhgrPnzjI-pPXfgLijBBaT4mc0Zj0oJ2LDd9xiJCrmC0R0k_foB2N1Wb1PmaC5t7CT2VkL2p3aue3sbuFnSv91Z0meGZzasHp91J89gsWxftujF1kCx84Co4ALXV76e4dFhjZ68" else photoUrl,
                isFavorite = isFavorite
            )
        }
    }

    // Intents
    fun openInGoogleMaps(context: Context, address: String) {
        if (address.isBlank()) return
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        
        // Fallback to web browser if Maps app is not installed
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(address)))
            context.startActivity(webIntent)
        }
    }

    fun makeRealCall(context: Context, phone: String) {
        if (phone.isBlank()) return
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phone)))
        context.startActivity(intent)
    }

    fun sendEmail(context: Context, name: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, "Contacto Sahara Connect")
            putExtra(Intent.EXTRA_TEXT, "Hola $name,\n\nTe escribo desde Sahara Connect.")
        }
        context.startActivity(Intent.createChooser(intent, "Enviar correo"))
    }
}
