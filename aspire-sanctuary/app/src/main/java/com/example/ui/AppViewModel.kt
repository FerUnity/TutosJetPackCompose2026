package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.Booking
import com.example.data.Property
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    init {
        val appDao = AppDatabase.getDatabase(application).appDao()
        repository = AppRepository(appDao)
        viewModelScope.launch {
            repository.checkAndPrepopulate()
        }
    }

    val allProperties: StateFlow<List<Property>> = repository.allProperties
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteProperties: StateFlow<List<Property>> = repository.favoriteProperties
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Filters
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val selectedBudgetMax = MutableStateFlow<Double?>(null)

    // Combined/Filtered properties
    val filteredProperties: StateFlow<List<Property>> = combine(
        allProperties,
        searchQuery,
        selectedCategory,
        selectedBudgetMax
    ) { properties, query, category, maxBudget ->
        properties.filter { property ->
            val matchesQuery = query.isEmpty() || 
                    property.name.contains(query, ignoreCase = true) || 
                    property.location.contains(query, ignoreCase = true)

            val matchesCategory = category == "All" || property.category.equals(category, ignoreCase = true)

            val matchesBudget = maxBudget == null || property.pricePerNight <= maxBudget

            matchesQuery && matchesCategory && matchesBudget
        }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Details navigation support
    private val _selectedPropertyId = MutableStateFlow<Int?>(null)
    val selectedPropertyId: StateFlow<Int?> = _selectedPropertyId.asStateFlow()

    val selectedProperty: StateFlow<Property?> = _selectedPropertyId
        .flatMapLatest { id ->
            if (id == null) flowOf(null)
            else repository.getPropertyById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectProperty(id: Int?) {
        _selectedPropertyId.value = id
    }

    fun toggleFavorite(propertyId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(propertyId)
        }
    }

    fun setFavorite(propertyId: Int, isFav: Boolean) {
        viewModelScope.launch {
            repository.setFavorite(propertyId, isFav)
        }
    }

    fun bookProperty(
        propertyId: Int,
        propertyName: String,
        propertyLocation: String,
        propertyImageUrl: String,
        checkInDate: String,
        checkOutDate: String,
        totalPrice: Double,
        guestsCount: Int
    ) {
        viewModelScope.launch {
            repository.bookProperty(
                Booking(
                    propertyId = propertyId,
                    propertyName = propertyName,
                    propertyLocation = propertyLocation,
                    propertyImageUrl = propertyImageUrl,
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    totalPrice = totalPrice,
                    guestsCount = guestsCount
                )
            )
        }
    }

    fun cancelBooking(booking: Booking) {
        viewModelScope.launch {
            repository.cancelBooking(booking)
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setCategory(category: String) {
        selectedCategory.value = category
    }

    fun setBudgetMax(max: Double?) {
        selectedBudgetMax.value = max
    }
}
