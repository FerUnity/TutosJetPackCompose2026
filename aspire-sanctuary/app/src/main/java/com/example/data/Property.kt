package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class Property(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val location: String,
    val category: String, // Villas, Mansions, Penthouse, Ski Chalets, Estates, Islands
    val pricePerNight: Double,
    val description: String,
    val beds: Int,
    val baths: Int,
    val areaSqM: Int,
    val imageUrlsCommaSeparated: String,
    val isFavorite: Boolean = false,
    val amenitiesCommaSeparated: String
) {
    val imageUrlList: List<String>
        get() = imageUrlsCommaSeparated.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    val amenityList: List<String>
        get() = amenitiesCommaSeparated.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
