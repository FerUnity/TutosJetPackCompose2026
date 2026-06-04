package com.example.swiperefresh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiperefresh.home.HomeScreen
import com.example.swiperefresh.home.HomeViewmodel
import com.example.swiperefresh.navigation.AppNavigation
import com.example.swiperefresh.ui.theme.SwipeRefreshTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = HomeViewmodel()
        setContent {
            SwipeRefreshTheme {
                Surface(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background,
                    shape = androidx.compose.material3.MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxSize().padding(15.dp)
                ) {
                    AppNavigation(viewModel)
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SwipeRefreshTheme {
        Greeting("Android")
    }
}