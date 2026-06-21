package com.example.proyectocasaspildoras.data


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectocasaspildoras.R

@Composable
fun CardCasas(
    navController: NavController,
    casa: Casa
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("detalle/${casa.id}") },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
    {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            //            Debemos mostrar la imagen dependiendo si tiene imagenUri tomada desde la galeria
            // o tiene imagenId que la toma desde la carpeta drawable:

//            Primero si toma la casa desde la galeria o camara, desde el formulario:
            if (casa.imagenUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(casa.imagenUri),
                    contentDescription = casa.nombre,
                    modifier = Modifier.size(80.dp) //tamaño de la imagen en la Card
                )
            } else
//            Se toma la imagen de la casa desde la carpeta drawable:
            {
                Image(
                    painter = painterResource(id = casa.imagenId ?: R.drawable.casa1),
       //Se agrega !! porque sabemos que no es null, o bien un operador elvis para que muestre otra imagen por defecto: casa1.
                    contentDescription = casa.nombre,
                    modifier = Modifier.size(80.dp) //tamaño de la imagen en la Card
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column() {
                Text(
                    text = casa.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = casa.descripcion,
                    maxLines = 2, //Que ocupe 2 lineas indep del largfo de la descripcion
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

