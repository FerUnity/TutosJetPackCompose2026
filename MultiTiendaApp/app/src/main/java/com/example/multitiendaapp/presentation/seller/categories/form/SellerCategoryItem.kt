package com.example.multitiendaapp.presentation.seller.categories.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multitiendaapp.core.model.Category

//Este es un componenete reutilizable que representara a
// cada elemento de la lista de categorias en la pantalla de lista de categorias: SellerCategoriesScreen.kt.
//O sea, cada fila de la lista de categorias por tienda, sera una instancia de este componente o composable:

@Composable
fun SellerCategoryItem(
    category: Category
//Esta es la categoria que se mostrara en la fila de la lista de categorias
// y provendra desde la pantalla padre SellerCategoriesScreen.kt
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), //Para redondear las esquinas de la tarjeta
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) //elevation es para dar efecto de una sombra a la tarjeta:

    ) {
        Column(
            modifier = Modifier
                .padding(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
            //Para dar espacio vertical automatico entre los elementos hijos de la columna
        ) {
            Text(
                text = category.name, //Aca se mostrara el nombre de la categoria
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

