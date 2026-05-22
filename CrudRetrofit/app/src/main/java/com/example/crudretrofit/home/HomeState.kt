package com.example.crudretrofit.home

data class HomeState(
    val productId: String? = null,
    val productName: String = "",
    val productPrice: String = "",
    val products: List<Product> = emptyList()
)
