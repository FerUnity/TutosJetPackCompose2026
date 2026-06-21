package com.example.proyectocasaspildoras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.proyectocasaspildoras.navigation.AppNavigation
import com.example.proyectocasaspildoras.ui.theme.ProyectoCasasPildorasTheme
import com.example.proyectocasaspildoras.viewmodel.ViewModelRegistro

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val registroViewModel by viewModels<ViewModelRegistro>()
        setContent {
            ProyectoCasasPildorasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding),
                        registroViewModel = registroViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ProyectoCasasPildorasTheme {
//        AppNavigation(modifier = Modifier.fillMaxSize(), registroViewModel = registroViewModel)
    }
}