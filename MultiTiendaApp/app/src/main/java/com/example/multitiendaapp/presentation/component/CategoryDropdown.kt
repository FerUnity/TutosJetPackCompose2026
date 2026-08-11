package com.example.multitiendaapp.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


//Este componente permite al vendedor seleccionar la categoria a la cual pertence su tienda,
// y se mostrara en la pantalla en el Registro de tienda, o sea en RegisterStoreScreen()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategory: String, //Aca se guardara la categoria seleccionada por el vendedor.
    onCategorySelected: (String) -> Unit //Aca se define una fun que se ejecutara cuando se seleccione una categoria,
// que se guardo en la var selectedCategory.
) {
    //Primero def si el menu desplegable esta abierto o cerrado en RegisterStoreScreen:
    var expanded by remember { mutableStateOf(false) }
    //Se usa el remember para que conserve su estado entre recomposiciones: giro pantallas, etc

//    Creamos las cetgorias a mostrar en el menu desplegable:
    val categories = listOf(
        "Arte y manualidades",
        "Automotriz",
        "Belleza y cuidado personal",
        "Celulares y accesorios",
        "Computación",
        "Deportes y ocio",
        "Electrodomesticos",
        "Hogar",
        "juguetes",
        "Libros",
        "Musica",
        "Ropa",
        "Salud y bienestar"
    )

//    Ahora creamos un menu desplegable con las categorias recien listadas:
    ExposedDropdownMenuBox(
        expanded = expanded, //Aca se define si el menu desplegable esta abierto o cerrado
        onExpandedChange = { expanded = it }
        //Aca se define el cambio de estado del menu desplegable al hacer click en el icono de flecha.
    ) {
        //Aca creamos el campo de texto para seleccionar la categoria:
        OutlinedTextField(
            value = selectedCategory, //Aca se define el valor del campo de texto, que es la categoria seleccionada
            onValueChange = {}, //Aca se define la fun que se ejecutara cuando se modifique el valor del campo de texto. sera NADA.
            readOnly = true, //Aca se define que el campo de texto sera de solo lectura. No se pueda modificar.
            modifier = Modifier
                .fillMaxWidth() //Aca se define el ancho del campo de texto.
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ), //Aca se define que este campo de texto sera el ancla del menu desplegable.
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Icono de categoria"

                )
            },
            label = { Text(text = "Categoria de la tienda") },

            trailingIcon = { //Aca se define el icono de flecha que se mostrara al final del campo de texto. Al reves del leadingIcon.
                //Aca se muestra la flecha hacia arriba si el menu desplegable esta abierto y hacia abajo si esta cerrado.
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded //Aca se define si el menu desplegable esta abierto o cerrado.
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category) },
                    onClick = {
                        onCategorySelected(category) //Aca enviamos la categoria seleccionada al componenete padre que es RegisterStoreScreen()
                        expanded =
                            false //Aca hacemos que el menu desplegable se cierre al seleccionar una categoria
                    }
                )
            }
        }
    }
}