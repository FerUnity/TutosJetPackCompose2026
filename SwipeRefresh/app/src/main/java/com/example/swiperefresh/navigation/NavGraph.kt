package com.example.swiperefresh.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.swiperefresh.home.HomeScreen
import com.example.swiperefresh.home.HomeViewmodel
import com.example.swiperefresh.home.ListadoNumerico

@Composable
fun AppNavigation(
    viewModel: HomeViewmodel
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home")
    {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable("listadoNumerico") {
            ListadoNumerico(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}
