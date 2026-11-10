package com.example.multitiendaapp.presentation.seller.categories.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.multitiendaapp.core.model.Category

//Este es un componenete grafico (Card) reutilizable que contendra a
// cada categoria de la lista de categorias almacenada en Firestore,
// que se mostrara en la pantalla de lista de categorias: SellerCategoriesScreen.kt.
//O sea, cada fila(categoria) de la lista de categorias por tienda,
// que se mostrara en la pantalla de lista de categorias SellerCategoriesScreen.kt,
// sera una instancia de este componente o composable: SellerCategoryItem()

@Composable
fun SellerCategoryItem(
    category: Category
//Esta es la categoria que se mostrara en la Columna de la lista de categorias de la tienda del vendedor actual,(Seller)
// esta lista de categorias se mostrara en la pantalla de lista de categorias SellerCategoriesScreen.kt,
// y provendra desde Firestore.
//    onUpdate: (Category) -> Unit = {}, //Esta funcion recibe una categoria como parametro y no devuelve nada,
    // y se usa para actualizar la categoria que se recibio como parametro, en Firestore,
//    onDelete: (Category) -> Unit = {}, //Esta funcion recibe una categoria como parametro y no devuelve nada,
    // se usa para eliminar la categoria que se recibio como parametro, en Firestore,
) {
    /* Card(
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
     }*/

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp), //Para redondear las esquinas de la tarjeta
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), //elevation es para dar efecto de una sombra a la tarjeta:
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,//Para cambiar el color de fondo de la tarjeta
            contentColor = MaterialTheme.colorScheme.onSurface //Para cambiar el color del texto de la tarjeta
        )
    ) {
//        Generamos un organizador horizontal de los elementos, con Row:
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,//Para centrar verticalmente los elementos hijos de la fila
            horizontalArrangement = Arrangement.SpaceBetween //Para dar espacio horizontal entre los elementos hijos de la fila
        ) {
//            Creamos una fila izquierda de elementos:
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp) // Para establecer el tamaño del contenido del Box
                        .clip(
                            RoundedCornerShape(16.dp) //Para redondear las esquinas del contenido del Box
                        )
                        .background(MaterialTheme.colorScheme.primaryContainer), //Para cambiar el color de fondo del Box
                    contentAlignment = Alignment.Center //Para centrar el contenido del Box de forma automática
                ) {
//                    Creamos un icono que representa a las categorias dentro del Box:
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Icono de categoria",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp) //Para establecer el tamaño del icono
                    )
                } // Cierre del Box:

                Spacer(modifier = Modifier.width(16.dp)) // Para dar un espacio entre el icono y el texto

                Text(
                    text = category.name, //Aca se mostrara el nombre de la categoria
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold //Para poner el texto en negrita
                )

            } // Cierre de la fila izquierda de elementos.
//            Creamos otro Row a la derecha de la fila izq:
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Creamos un boton (IconButton) para actualizar la categoria:
                IconButton(
                    onClick = {
//                        onUpdate(category)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Actualizar categoria",
                        tint = MaterialTheme.colorScheme.primary
                    )

                }

//                Creamos otro boton (IconButton) para eliminar la categoria:
                IconButton(
                    onClick = {
//                        onDelete(category)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar categoria",
                        tint = MaterialTheme.colorScheme.error //Color rojo para el icono de eliminar
                    )
                }
            }

        } // Cierre de la fila de elementos:
    }
}



