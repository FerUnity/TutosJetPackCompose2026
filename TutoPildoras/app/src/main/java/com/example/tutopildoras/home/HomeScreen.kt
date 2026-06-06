package com.example.tutopildoras.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(modifier: Modifier) {
    Box(modifier = Modifier.fillMaxSize().padding(50.dp),
        contentAlignment = Alignment.TopEnd){
        Column() {
            Text(text = "Primer Box")
            Text(text = "Primer Box")
            Text(text = "Primer Box")
            Text(text = "Primer Box")

        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(50.dp),){
//        El problema del Box es que se superponen los elementos si no estam en una Column o Row,
        //        por eso se usa el  Modifier.align(Alignment...) en cada uno de los elementos del Box:
        Text(text = "Segundo Box", modifier = Modifier.align(Alignment.TopStart))
        Text(text = "Ese Box", modifier = Modifier.align(Alignment.Center))
        Text(text = "Este Box", modifier = Modifier.align(Alignment.BottomEnd))
    }
}