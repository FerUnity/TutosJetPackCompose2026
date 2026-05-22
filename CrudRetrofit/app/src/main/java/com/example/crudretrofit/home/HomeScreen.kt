package com.example.crudretrofit.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    modifier: Modifier
) {
    val state = homeViewModel.state

    Column(
        modifier = modifier.fillMaxSize().padding(top =  16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Mis Productos", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        TextField(
            value = state.productName,
            onValueChange = { homeViewModel.changeName(it) },
            placeholder = {
                Text(text = "Nombre del producto")
            }
        )

        TextField(
            value = state.productPrice,
            onValueChange = { homeViewModel.changePrice(it) },
            placeholder = {
                Text(text = "Precio del producto")
            }

        )

        Button(onClick = { homeViewModel.createProduct() }) {
            Text(text = "Agregar Producto")
        }

//        Aca llamamos a la lista de productos agregados a la BBDD de la API, en una lazyColumn que contendra los productos
    //        en el formato de filas: ProductItem.kt:
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(state.products) { product ->
                ProductItem(
                    product = product,
                    modifier = Modifier.fillMaxWidth(),
                    onEdit = { homeViewModel.editProduct(product) },
                    onDelete = { homeViewModel.deleteProduct(product) }
                )
            }
        }
    }
}

