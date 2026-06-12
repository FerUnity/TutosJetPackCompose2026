package com.example.tutopildoras.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.ui.graphics.graphicsLayer
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

//    Para centrar la imagen debemos saber su ancho y alto de forma similar:
    var anchoImagen by remember { mutableStateOf(0f) }
    var altoImagen by remember { mutableStateOf(0f) }


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

//        Haremos que sea posible hacer zoom a la imagen usando el gesto de dos dedos.
        //        Llamamos a la fun composable donde ademas se crea la Imagen:
        ImagenInteractiva()

//        La imagen estara movil con offset y centrada solo usando modifier.align(Alignment.Center):
        /*     Image(
                 painter = painterResource(id = R.drawable.lamborgmiura),
                 contentDescription = "Lamborghini Miura",
                 modifier = Modifier
                     .fillMaxSize()
                     .align(Alignment.Center)
                        //Usamos offset para mover la imagen en la pantalla:
                  .offset { IntOffset(imagen.x.toInt(), imagen.y.toInt()) }
                  .pointerInput(Unit) {
                      detectDragGestures { change, dragAmount ->
                          change.consume()
                          imagen += Offset(dragAmount.x, dragAmount.y)
                      }
                  }
             )*/

//        Que la imagen sea movil:
        /* Image(
             painter = painterResource(id = R.drawable.lamborgmiura),
             contentDescription = "Lamborghini Miura",
             modifier = Modifier
                 .fillMaxSize()
 //                .align(Alignment.Center)
                 //                Obtenemos el ancho y alto de la imagen:
                 .onGloballyPositioned { coordinates ->
                     anchoImagen = coordinates.size.width.toFloat()
                     altoImagen = coordinates.size.height.toFloat()

 //                FInalmente centramos la imagen en el centro de la pantalla
 //                tanto en el eje x como en el eje y, asi:
                     if (positionText == Offset(0f, 0f)) {
                         positionText = Offset(
                             (anchoPantalla - anchoImagen) / 2, (altoPantalla - altoImagen) / 2)
                     }
                 }
 //                FIn centrado de la imagen.
                 //Usamos offset para mover la imagen en la pantalla:
                 .offset { IntOffset(imagen.x.toInt(), imagen.y.toInt()) }
                 .pointerInput(Unit) {
                     detectDragGestures { change, dragAmount ->
                         change.consume()
                         imagen += Offset(dragAmount.x, dragAmount.y)
                     }
                 }
         )*/
//        Este el texto se superpone sobre la imagen.
//        PEro queremos que el texto este centrado al inicio.
//        Para eso debemos saber el ancho y alto del texto, en el modifier del texto:
        Text(
            text = "Lamborghini Miura",
//            modifier = Modifier.align(Alignment.Center),
//          Alineacion en el centro, del texto respecto a la pantalla completa.

            //Alineamos el texto de forma centrada en la pantalla completa,
            // usando la var positionText:
            modifier = Modifier
//                Obtenemos el ancho y alto del texto:
                .onGloballyPositioned { coordinates ->
                    anchoTexto = coordinates.size.width.toFloat()
                    altoTexto = coordinates.size.height.toFloat()

//                FInalmente centramos el texto en el centro de la pantalla
//                tanto en el eje x como en el eje y, asi:
                    if (positionText == Offset(0f, 0f)) {
                        positionText = Offset(
                            (anchoPantalla - anchoTexto) / 2, (altoPantalla - altoTexto) / 2
                        )
                    }
                }
//                FIn centrado del texto.

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
            textAlign = TextAlign.Center, //Alineacion centrada del texto pero en el parrafo.
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

//Fun que crea la imagen en un Box y ademas permiter hacerle zoom,desplazar y rotar.
// Para eso usamos la fun del modifier: pointerInput()
// hacemos posible hacer zoom,desplazar y rotar la imagen usando el gesto de dos dedos,
// con su fun detectTransformGestures(centro del zoom, desplazamiento, zoom, rotation) del pointerInput():
@Composable
fun ImagenInteractiva() {
    //Escala de la imagen: var que almacena el zoom de la imagen, cuyo valor se va actualizando con el gesto de dos dedos:
//    Su valor inicial es 1f, que es la escala normal de la imagen.
    var escala by remember { mutableStateOf(1f) }

    //Posicion de la imagen: var que almacena la posicion de la imagen, cuyo valor se va actualizando con el gesto de dos dedos:
//    Su valor inicial es Offset(0f, 0f), que es la posicion normal de la imagen.
    var position by remember { mutableStateOf(Offset(0f, 0f)) }

//    Angulo de rotacion: var que almacena el angulo de rotacion de la imagen, cuyo valor se va actualizando con el gesto de dos dedos:
//    Su valor inicial es 0f, que es el angulo de rotacion normal de la imagen (Sin rotacion)
    var rotation by remember { mutableStateOf(0f) }

//    Generamos un Box que contenga a la imagen:
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
//                Usamos la fun de extension del modifier pointerInput(), detectTransformGestures() para detectar el gesto de dos dedos.
                // Esto para poder hacer zoom,desplazar y rotar la imagen:
                detectTransformGestures { _, desplazamiento, zoom, anguloRotacion ->
                    escala *= zoom //Aplica zoom. Se usa *= y no += para que el zoom sea mas rapido
                    position += desplazamiento //Aplica desplazamiento. Se usa += para que la imagen se mueva con el gesto de dos dedos.
                    rotation += anguloRotacion //Aplica rotacion. Se usa += para que la imagen se rote con el gesto de dos dedos.
                }

            }
            .pointerInput(Unit) {
                //De la misma forma con un nueva fun de extension del modifier pointerInput(),
                // usamos detectTapGestures() para detectar el gesto de tap en la imagen.
                //Con esto logramos que con un doble toque (onDoubleTap) en la imagen, logremos volver a su posicion original, escala y rotacion inicial:
                detectTapGestures(onDoubleTap = { _ ->
                    escala = 1f //Reset de la escala.
                    position = Offset(0f, 0f) //Reset de la posicion.
                    rotation = 0f //Reset de la rotacion.
                })
            },
        contentAlignment = Alignment.Center //Para centrar la imagen en el centro del Box.
    )
    {
        Image(
            painter = painterResource(id = R.drawable.lamborgmiura),
            contentDescription = "Lamborghini Miura",
            modifier = Modifier
//          Usamos la funcion de extension graphicsLayer(),
                //   Para poder usar el zoom, desplazamiento y rotacion de la imagen, usando el gesto de dos dedos:
                .graphicsLayer(
//                    Zoom en X e Y:
                    scaleX = escala.coerceIn(
                        0.5f,
                        3f
                    ),//En X:Minimo zoomeado = 0.5 y maximo limite de zoom = 3
                    scaleY = escala.coerceIn(0.5f, 3f),// Idem en Y
//                    Desplazamiento en X e Y:
                    translationX = position.x,
                    translationY = position.y,
                    rotationZ = rotation //Rotacion en Z con el valor de la var rotation.
                )

        )

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