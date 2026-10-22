package com.example.multitiendaapp.presentation.seller.categories.form

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.multitiendaapp.R

//Este formulario es para que el usuario VENDEDOR, pueda CREAR categorias a las tiendas,
// quedando registradas en la BBDD firebase:
// la parte logica de este formulario esta en SellerCategoryFormViewModel.kt,
// Esta vista hay que registrarla en contenedor de SellerRootScreen()
@Composable
fun SellerCategoryFormScreen(
    viewModel: SellerCategoryFormViewModel,
    onBack: () -> Unit
) {
//    Creamos un estado para escuchar u observar el estado del viewModel de esta pantalla de creacion de categorias,
    //    esto es para que cuando haya un cambio en esta vista, se recomponga de inmediato:
    val state by viewModel.uiState.collectAsStateWithLifecycle()

//    Ademas un contexto para identificar esta app frente al OS Android,
//    y asi mostrar mensajes Toast, navegar a otras pantallas, etc:
    val context = LocalContext.current

//    Creamos un Launcheffect para escuchar los efectos que vienen del viewModel, como
//    ShowMessage y NavigateBack, para mostrar mensajes Toast y navegar a otras pantallas, etc:
    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                //Aca se procesa el efecto ShowMessage, y se define que se mostraran como mensajes Toast:
                is SellerCategoryFormEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                //Aca se procesa el efecto NavigateBack, para navegar a la pantalla anterior,
                is SellerCategoryFormEffect.NavigateBack -> {
                    onBack()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            //Para dar espacio vertical de 12.dp, entre los elementos hijos de la columna
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Crear categoria",
                    style = MaterialTheme.typography.titleMedium
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_category),
                    contentDescription = "Icono de categoria",
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                )

//                Campo de texto donde ira la categoria:
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.name, //El valor mostrado viene directamente desde el viewModel,
                    onValueChange = { newCategory ->
                        viewModel.onEvent(SellerCategoryFormEvent.OnNameChange(newCategory))
//                        O sea aca se envia el evento OnNameChange al viewModel,
      //                    entonces el viewmodel recibe el evento y lo procesa, ejecutando la fun updateName(),
                         // que corresponde al evento OnNameChange,
                    },
                    label = {
                        Text(text = "Nombre de la categoria")
                    },
                    singleLine = true, //Solo 1 linea
                    enabled = !state.isLoading //Para habilitar el campo de texto, si se esta guardando la categoria, no se puede editar. Y viceversa
                )

//                Aca creamos un mensaje de error, si hay uno:
                state.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error //Para darle color rojo de error al mensaje de error
                    )
                }

//                Boton para agregar la categoria:
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        viewModel.onEvent(SellerCategoryFormEvent.OnCreateClick)
                        //O sea aca se envia el evento OnCreateClick al viewModel,
                        // entonces el viewmodel recibe el evento y lo procesa, ejecutando la fun createCategory(),
                    },
                    enabled = !state.isLoading //Para habilitar el boton segun este cargando o no.
                ) {
                    Text(text = "Agregar categoria")
                }

            }
        }
    }

}