package com.example.persistenciaroom.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.persistenciaroom.data.Tarea

@Composable
fun PantallaTareas(tareaViewModel: TareaViewModel, modifier: Modifier = Modifier) {
    val listaTareas by tareaViewModel.tareas.collectAsState(initial = emptyList())
//    Se usa collectAsState para convertir el Flow de tareas en un estado de Compose que se puede redibujar cuando cambia.

    var texto by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (texto.isNotBlank()) {
                    tareaViewModel.agregarTarea(texto)
                    texto = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(listaTareas) { tarea ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
//  Al hacer click sobre la card se borra la tarea que contiene:
                        .clickable {
                            tareaViewModel.borrarTarea(tarea)
                        }
                ) {
                    Text(
                        text = tarea.titulo,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
