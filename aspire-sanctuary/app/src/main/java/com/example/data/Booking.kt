package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val propertyId: Int,
    val propertyName: String,
    val propertyLocation: String,
    val propertyImageUrl: String,
    val checkInDate: String,
    val checkOutDate: String,
    val totalPrice: Double,
    val guestsCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)
