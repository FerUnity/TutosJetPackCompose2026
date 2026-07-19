package com.example.data.model

data class Contact(
    val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val company: String,
    val photoUri: String,
    val isFavorite: Boolean,
    val isSystemContact: Boolean
)
