package com.example.multitiendaapp.presentation.seller.categories.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multitiendaapp.R

//Esta vista hay registrarla en contenedor de SellerRootScreen()
@Composable
fun SellerCategoryFormScreen(){
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    )
    {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),//Para redondear las esquinas de la tarjeta
//            elevation es para dar una sombra a la tarjeta:
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
//            Column para organizar verticalmente los elementos de la tarjeta:
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)//Para dar espacio vertical entre los elementos hijos de la columna
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Crear nueva categoria",
                    style = MaterialTheme.typography.titleMedium
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_category),
                    contentDescription = "Icono de categoria",
                    modifier = Modifier.size(80.dp).align(Alignment.CenterHorizontally)
                )

//                Campo de texto donde ira la categoria:
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {},
                    label = {
                        Text(text = "Nombre de la categoria")
                    },
                    singleLine = true, //Solo 1 linea
                    enabled = true //Para habilitar el campo de texto
                )

//                Boton para
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    onClick = { /*TODO*/ },
                    enabled = true //Para habilitar el boton
                ) {
                    Text(text = "Agregar categoria")
                }

            }
        }
    }

}