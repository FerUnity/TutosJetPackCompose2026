package com.example.proyectocasaspildoras.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectocasaspildoras.R
import com.example.proyectocasaspildoras.data.Casa
import com.example.proyectocasaspildoras.data.RepositorioCasa
import kotlin.random.Random

@Composable
fun DetalleCasas(
    navController: NavHostController,
    casaId: Int
) {
    val casa = RepositorioCasa.obtenerCasaPorId(casaId) ?: return
    //Obtenemos la casa segun su id(param casaId) recibido por parametro desde Galeria y Card.
    // Y usando el operador de llamada Elvis, que si no existe id devuelve null y no se cae la app.

//    Para cambiar el valor de color de fondo
    var colorFondo by remember { mutableStateOf(Color.White) }

//    Para mover la img de la casa:
    var offset by remember { mutableStateOf(Offset.Zero) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo) //Aca decimos que el color de fondo sera el de la var colorFondo.
            .padding(16.dp)
    ) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.Start) //Para que el boton se alinee arriba a la izquierda de la pantalla
                .padding(16.dp)
        ) {
            Text(text = "Volver", style = MaterialTheme.typography.titleSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = casa.nombre, style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)) //Para que el texto se alinee al centro de la pantalla.

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
//                Para arrastrar la imagen de la casa con el dedo:
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, rotationDelta ->
                        offset += pan //Actualizamos la posicion del box con el pan, que contiene a la imagen de la casa.
                    }
                },
            contentAlignment = Alignment.Center //Para que el Box se alinee al centro de la pantalla.
        ){
//            Debemos mostrar la imagen dependiendo si tiene imagenUri tomada desde la galeria
            // o tiene imagenId que la toma desde la carpeta drawable:

            //Primero: si es que la imagenUri no es nula, significa que tomara la imagen de la galeria:
            // Por ende toma la casa desde la galeria o camara, desde el formulario:
            if (casa.imagenUri != null) {
                Image(
//                painter = painterResource(id = casa.imagenId),
                    painter = rememberAsyncImagePainter(casa.imagenUri),
                    contentDescription = casa.nombre,
//                Para mover la imagen con los dedos:
                    modifier = Modifier
                        .graphicsLayer (
                            translationX = offset.x,
                            translationY = offset.y
                        )
                )
            }
            else
//  Sino significa que es una de las casas que vienen en la carpeta drawable:
            //  Por tanto se toma la imagen de la casa desde la carpeta drawable:
            {
                Image(
                    painter = painterResource(id = casa.imagenId ?: R.drawable.casa1),
     //Se agrega !! porque sabemos que no es null,
                    // o bien un operador elvis ?: si es que fuera nula la img, para que muestre otra imagen por defecto: casa1.
                    contentDescription = casa.nombre,
//                    Para mover la imagen con los dedos:
                    modifier = Modifier
                        .graphicsLayer (
                    translationX = offset.x,
                    translationY = offset.y
                )
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = casa.descripcion, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { colorFondo = Color(CambioFondo()) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Cambiar Fondo", style = MaterialTheme.typography.titleSmall)
        }
    }

}

//Fun para cambiar de forma aleatoria, el color en HEX de fondo de la pantalla:
fun CambioFondo(): Long {
    return (0xFFFFFF..0xFFFFFFFF).random()

}

//Fun que genera un color aleatorio en RGB:
fun colorAleatorio(): Color {
    val red = (0..255).random()
    val green = (0..255).random()
    val blue = (0..255).random()
    return Color(red, green, blue)

}

//Version 3 fun cambiar color en RGB v2:
fun cambiarColor(): Color {
    return Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
}