package com.example.crudretrofit.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productService: ProductService
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            try {
                val products = productService.getProducts()
                state = state.copy(
                    products = products
                )
            } catch (e: Exception) {
                // Es importante imprimir el error para depurar si la API falla (ej. endpoint expirado)
                e.printStackTrace()
            }
        }
    }

    fun changeName(name: String) {
        state = state.copy(
            productName = name
        )
    }

    fun changePrice(price: String) {
        state = state.copy(
            productPrice = price
        )
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                productService.deleteProduct(product.id)
                getProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun editProduct(product: Product) {
        state = state.copy(
            productId = product.id,
            productName = product.name,
            productPrice = product.price.toString()
        )
    }

    fun createProduct() {
        // Validamos que el precio sea un número válido antes de intentar la operación
        val price = state.productPrice.toDoubleOrNull() ?: 0.0
        
        val productDto = ProductDto(
            name = state.productName,
            price = price
        )

        viewModelScope.launch {
            try {
                if (state.productId == null) {
                    productService.insertProduct(productDto)
                } else {
                    productService.updateProduct(productDto, state.productId!!)
                }
                // Después de insertar o actualizar, limpiamos el formulario y refrescamos la lista
                state = state.copy(
                    productId = null,
                    productName = "",
                    productPrice = ""
                )
                getProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
