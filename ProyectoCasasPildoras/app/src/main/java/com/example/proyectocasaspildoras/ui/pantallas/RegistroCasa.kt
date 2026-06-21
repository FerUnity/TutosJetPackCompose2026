package com.example.proyectocasaspildoras.ui.pantallas

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.proyectocasaspildoras.R
import com.example.proyectocasaspildoras.data.Casa
import com.example.proyectocasaspildoras.data.RepositorioCasa
import com.example.proyectocasaspildoras.viewmodel.ViewModelRegistro

//ESTE COMPOSABLE ES UN FORMULARIO PARA REGISTRAR UNA NUEVA CASA:
@Composable
fun RegistroCasa(
    navController: NavHostController,
    registroViewModel: ViewModelRegistro
) {
    /* Estas var las obtenemos del ViewModel:
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenId by remember { mutableStateOf(R.drawable.casa1) }*/

    //    Debemnos generar un launcher o lanzador, que nos permita iniciar otra actividad,
//    para seleccionar una imagen de la galeria o la camara de fotos.
//    Por ejemplo: la galeria de fotos o la camara de fotos, etc.

//    Este launcher lo llamamos en el boton de seleccionar imagen:
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        registroViewModel.imagenUri = uri
    }
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Registro de la nueva casa", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

//        Creamos un campo de texto con el cuadro delineado es: OutLinedTextField
        OutlinedTextField(
            value = registroViewModel.nombre,
            onValueChange = { registroViewModel.onNombreChange(it) },
            //Es lo mismo que poner: onValueChange = {nombre = it}
            label = { Text("Nombre de la casa") },
            isError = registroViewModel.nombre.isBlank()
            //Significa que si el nombre esta vacio, mostrara un error. El recuadro se pinta de rojo.
        )

        Spacer(modifier = Modifier.height(16.dp))

        //        Boton para seleccionar la imagen:
        Button(
            onClick = {
                launcher.launch("image/*")
                //Usamos im
            }
        ) {
            Text(text = "Seleccionar imagen")
        }

//        Vista previa de la imagen seleccionada usando la libreria coil, glide o picasso,
//        que tienen la ventaja de que se adaptan a cualquier tamaño de pantalla.
//        Usaremos la libreria coil:
//        let permite ejecutar un bloque de codigo si la variable no es nula y si fuera nula no se ejecuta el bloque de codigo:
        registroViewModel.imagenUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = coil.compose.rememberAsyncImagePainter(it),
                //it se refiere al valor de la ruta o Uri de la imagen seleccionada.
                // painter es una funcion que recibe un valor y devuelve un painter.
                contentDescription = "Imagen de la casa seleccionada",
                modifier = Modifier.fillMaxWidth().height(200.dp)
            /*Para que la imagen ocupe el ancho completo y 200 dp de altura.*/
            )
        }




        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = registroViewModel.descripcion,
            onValueChange = { registroViewModel.onDescripcionChange(it) },
            //Es lo mismo que poner: onValueChange = {descripcion = it}
            label = { Text("Descripcion de la casa") },
            isError = registroViewModel.descripcion.length < 10
            //Significa que si la descripcion tiene menos de 10 caracteres, mostrara un error. El recuadro se pinta de rojo.
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            /*Boton para guardar la casa en la base de datos y volver a la pantalla de inicio:
             pero se debe validar que:
             El nombre tenga contenido, que la descripcion tenga al menos 10 caracteres y que la nueva casa tenga una imagen.*/
            onClick = {
                if (registroViewModel.nombre.isNotBlank()
                    && registroViewModel.descripcion.length >= 10
                    && registroViewModel.imagenUri != null)
                {
                    //Guardar la nueva casa en la lista mutable listaCasas(),
                    // ubicada en el objeto RepositorioCasa, de la data class Casa.
                    // Para eso debemos crear un nuevo objeto casa con los datos del formulario
                    // y luego agregarlo a la lista usando la fun agregarCasa:
                    val nuevaCasa = Casa(
                        id = RepositorioCasa.listaCasas.size + 1, //El id se autoincrementa
                        nombre = registroViewModel.nombre, //El nombre se obtiene del ViewModel
                        imagenId = null, //Es null porque no se tiene una imagen en la carpeta drawable como las 3 anteriores
                        //sino que se selecciona una imagen de la galeria o la camara de fotos.
                        descripcion = registroViewModel.descripcion, //La descripcion se obtiene del ViewModel
                        imagenUri = registroViewModel.imagenUri //La imagen se obtiene de la galeria o la camara de fotos.
                    )
                    RepositorioCasa.agregarCasa(nuevaCasa)
                    //Volver a la pantalla de inicio:

                    navController.navigate("galeria")
                }
                else {
                    //Mostrar mensaje de error:
                    Toast.makeText(
                        navController.context,
                        "El nombre no puede estar vacio y la descripcion debe tener al menos 10 caracteres",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
        {
            Text(text = "Guardar nueva casa")
        }

    }
}

