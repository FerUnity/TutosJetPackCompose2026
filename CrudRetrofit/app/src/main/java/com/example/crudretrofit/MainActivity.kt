package com.example.crudretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.crudretrofit.home.HomeScreen
import com.example.crudretrofit.home.HomeViewModel
import com.example.crudretrofit.home.ProductService
import com.example.crudretrofit.ui.theme.CrudRetrofitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrudRetrofitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Usamos remember para evitar que el ViewModel se recree en cada recomposición
                    val homeViewModel = remember {
                        HomeViewModel(productService = ProductService.instance)
                    }
                    
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
