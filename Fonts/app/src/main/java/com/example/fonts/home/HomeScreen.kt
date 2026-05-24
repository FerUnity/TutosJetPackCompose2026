package com.example.fonts.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fonts.ui.theme.manropeFamily

@Composable
fun HomeScreen(modifier: Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hola. Texto normal", fontSize = 24.sp)
        Text(text = "Texto mediano", fontFamily = FontFamily.SansSerif, fontSize = 24.sp)
        Text(text = "Texto pequeño", fontFamily = FontFamily.Cursive, fontSize = 24.sp)
        Text(text = "Texto Manrope", fontFamily = manropeFamily, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 24.sp)
    }


}
