package com.example.tutopildoras.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutopildoras.R


//En este composable superpondremos una imagen y un texto
@Composable
fun HomeAlt(modifier: Modifier) {
//    Declaramos la var del color de fondo que cambiara de forma random al presionar un boton,
    //    siendo el blanco el color inicial.
    //    Como es un valor mutable debemos usar MutableStateOf(),
    //    ademas como ese valor debe ser recordado usamos remember cuando hay cambios de estado,
    //    por ej si hay otro boton que cambia otra cosa del UI
    //    y con by para delegar la propiedad de lectura y escritura a esa variable:
    var colorFondo by remember { mutableStateOf(Color.White) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .background(colorFondo)
    ) {
        Image(
            painter = painterResource(id = R.drawable.lamborgmiura),
            contentDescription = "Lamborghini Miura",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
//        Este el texto se superpone sobre la imagen.
        Text(
            text = "Lamborghini Miura",
            modifier = Modifier.align(Alignment.Center), //Alineacion respecto a la pantalla completa.
            textAlign = TextAlign.Center, //Alineacion del texto en el espacio disponible del parrafo.
            color = Color.Yellow,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

//        Ahora creamos un boton en la parte superior izq que cambiara el color de fondo de forma random.
//        Para eso creamos una funcion que devuelva un color aleatorio y la usamos como onClick:
        Button(
            onClick = {
//                colorFondo = colorAleatorio()
                colorFondo = Color(colorAleatorioHex())
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.Yellow
            ),
            modifier = Modifier.align(Alignment.TopStart) //en la parte superior izq de la pantalla.
        ) {
            Text(text = "Fondo")
        }
    }
}

//Fun que genera un color aleatorio en RGB:

fun colorAleatorio(): Color {
    val red = (0..255).random()
    val green = (0..255).random()
    val blue = (0..255).random()
    return Color(red, green, blue)

}

//Fun que genera un numero aleatorio en num Hexadecimal (Long) el
// cual hay que convertir a color usando Color() en la llamada a la fun:
fun colorAleatorioHex(): Long {
    return (0xFFFFFF..0xFFFFFFFF).random()

}