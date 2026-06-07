package com.example.tutopildoras.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
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

//    QUremos que el texto sea movil para eso hay que crear la var con remember y MutableStateOf():
    var positionText by remember { mutableStateOf(Offset(0f, 0f)) }

    var imagen by remember { mutableStateOf(Offset(0f, 0f)) }

//    Para poder centrar al inicio el texto o la imagen u otro elemento,
//    debemos almacenar el ancho y el largo de la pantalla del dispositivo.
//    Como no sabemos cual es el dispositivo debe ser un valor variable y recordable:
    var anchoPantalla by remember { mutableStateOf(0f) }
    var altoPantalla by remember { mutableStateOf(0f) }

//    Pero para que el centro de la pantalla coincida con el centro del texto, hay que saber el ancho y alto del texto:
    var anchoTexto by remember { mutableStateOf(0f) }
    var altoTexto by remember { mutableStateOf(0f) }


//    Para averiguar el ancho y alto de la pantalla debemos hacerlo en el elemento que lo contiene,
//    en este caso es el box que contiene a toda la pantalla:
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .background(colorFondo)
            .onGloballyPositioned { coordinates ->
                anchoPantalla = coordinates.size.width.toFloat()
                altoPantalla = coordinates.size.height.toFloat()
            }
    ) {

//        La imagen estara fija:
        /*   Image(
               painter = painterResource(id = R.drawable.lamborgmiura),
               contentDescription = "Lamborghini Miura",
               modifier = Modifier
                   .fillMaxSize()
                   .align(Alignment.Center)
           )*/

//        Que la imagen sea movil:
        Image(
            painter = painterResource(id = R.drawable.lamborgmiura),
            contentDescription = "Lamborghini Miura",
            modifier = Modifier
                .fillMaxSize()
//                .align(Alignment.Center)
                .offset { IntOffset(imagen.x.toInt(), imagen.y.toInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        imagen += Offset(dragAmount.x, dragAmount.y)
                    }
                }
        )
//        Este el texto se superpone sobre la imagen.
//        PEro queremos que el texto este centrado al inicio.
//        Para eso debemos saber el ancho y alto del texto, en el modifier del texto:
        Text(
            text = "Lamborghini Miura",
//            modifier = Modifier.align(Alignment.Center), //Alineacion en el centro, del texto respecto a la pantalla completa.
            //Alineacion en la pantalla completa, del texto, respecto a la var positionText:
            modifier = Modifier
//                Obtenemos el ancho y alto del texto:
                .onGloballyPositioned { coordinates ->
                    anchoTexto = coordinates.size.width.toFloat()
                    altoTexto = coordinates.size.height.toFloat()

//                FInalmente centramos el texto en el centro de la pantalla
//                tanto en el eje x como en el eje y, asi:
                    if (positionText == Offset(0f, 0f)) {
                        positionText = Offset(
                            (anchoPantalla - anchoTexto) / 2, (altoPantalla - altoTexto) / 2)
                    }
                }
//                Usamos offset para mover el texto en la pantalla:
                .offset { IntOffset(positionText.x.toInt(), positionText.y.toInt()) }
//                Usamos pointerInput para detectar el gesto de arrastre del texto(la presion y movimiento del dedo):
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        //Consume el evento de arrastre, para que no se propague a otros elementos, por ej si pasamos por encima de otro boton.
                        positionText += Offset(dragAmount.x, dragAmount.y)
                    }
                },
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