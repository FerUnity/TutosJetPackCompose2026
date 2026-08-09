package com.example.multitiendaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.multitiendaapp.navigation.AppNavHost
import com.example.multitiendaapp.ui.theme.MultiTiendaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiTiendaAppTheme {
                val navController = rememberNavController()
//                Creamos la var navController para poder navegar entre pantallas,
//                esta var la enviamos a AppNavHost para que pueda acceder a ella.
//                EL remember permite recordar el estado de la variable navController,
//                esto es util para recordar entre recomposiciones en que pantalla estamos, por ej al rotar la antalla
                AppNavHost(
                    navController = navController
                )
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MultiTiendaAppTheme {
        Greeting("Android")
    }
}