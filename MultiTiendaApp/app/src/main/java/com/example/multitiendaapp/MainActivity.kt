package com.example.multitiendaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.multitiendaapp.navigation.AppNavHost
import com.example.multitiendaapp.presentation.app.AppRoot
import com.example.multitiendaapp.ui.theme.MultiTiendaAppTheme
import dagger.hilt.android.AndroidEntryPoint

//Esto le dice a hilt que la clase MainActivity pueda inyectar dependencias en sus clases hijos,
// para que el viewmodel pueda ser inyectado.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiTiendaAppTheme {
             AppRoot()
            }
        }
    }
}