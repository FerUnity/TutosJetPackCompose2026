package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_contacts")
data class LocalContactEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val company: String,
    val photoUri: String,
    val isFavorite: Boolean = false,
    val isSystemContact: Boolean = false
)

@Entity(tableName = "call_records")
data class CallRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contactId: String,
    val name: String,
    val phone: String,
    val photoUri: String,
    val timestamp: Long,
    val durationSeconds: Int,
    val isOutgoing: Boolean = true
)

@Entity(tableName = "favorite_overrides")
data class FavoriteOverrideEntity(
    @PrimaryKey val contactId: String,
    val isFavorite: Boolean
)
