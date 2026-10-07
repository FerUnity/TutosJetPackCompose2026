package com.example.multitiendaapp.presentation.seller.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SellerCategoriesScreen(){
    //    Usaremos un Box para visyalizar sus contenido:
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineLarge
        )
    }

}