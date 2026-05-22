package com.example.crudretrofit.home

import com.squareup.moshi.Json

data class Product(
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "price")
    val price: Double
)

data class ProductDto(
    @Json(name = "name")
    val name: String,
    @Json(name = "price")
    val price: Double
)
