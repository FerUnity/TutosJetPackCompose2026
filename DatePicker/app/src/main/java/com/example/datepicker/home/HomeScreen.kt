package com.example.datepicker.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    // Var de estado para el DatePicker
    val state = rememberDatePickerState()

    // Var bool para mostrar o no el cuadro de dialogo del DatePicker:
    var showDatePicker by remember { mutableStateOf(false) }

    // Hacemos la logica para mostrar el cuadro de dialogo del DatePicker,
    // que ocurrira cuando se pulse el boton Mostrar Fecha.
    // El cuadr de dialogo o DatePickerDialog posee 3 botones:
    // Confirmar, Cancelar y fuera de el. Al presionar cualquiera de ellos, se ocultara el cuadro de dialogo.
    // Entonces le damos la funcionalidad a cada boton:
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false //Cerramos el cuadro de dialogo al pulsar fuera de el.
            },
            confirmButton = {
                Button( //Boton primario
                    onClick = {
                        showDatePicker = false //Cerramos el cuadro de dialogo al pulsar el boton confirmar,
                    //Luego de seleccionar una fecha, la guardamos en el state del DatePicker: .
                    }
                ) {
                    Text(text = "Confirmar")
                }
            },
            dismissButton = {
                OutlinedButton( //Boton secundario
                    onClick = {
                        showDatePicker = false //Cerramos el cuadro de dialogo al pulsar el boton cancelar.
                    }
                ) {
                    Text(text = "Cancelar")
                }
            },
        )
        {
//            Llamamos al DatePicker:
            DatePicker(state = state)
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Seleccione una fecha",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Boton para mostrar el DatePicker:
            Button(
                onClick = {
                    showDatePicker = true
                },
                modifier = Modifier.padding(16.dp),
            ) {
                Text(text = "Mostrar Fecha")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ahora mostramos la fecha seleccionada en el texto, usando el state del DatePicker.
            // La idea es mostrar el texto solo cuando se seleccione una fecha (o sea cuando la fecha no sea null),
            // para ello usamos el let:
            val date = state.selectedDateMillis
            // Y para mostrar el texto solo cuando se seleccione una fecha, hacemos:
            date?.let {
                // Convertimos la fecha desde millisegundos a un formato legible, en la var instant:
                val instant = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                Text(
                    text = "Fecha seleccionada: ${instant.dayOfMonth}/${instant.monthValue}/${instant.year}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }


}
