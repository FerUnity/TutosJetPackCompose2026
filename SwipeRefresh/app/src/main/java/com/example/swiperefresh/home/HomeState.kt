package com.example.swiperefresh.home

data class HomeState(
    val currentValue: Int = 0, //Va ir incrementandose este valor en cada refresh
    val isLoading: Boolean = false //Valor que se llama en el viewmodel y que muestra si se esta cargando con el swipe refresh
) {

}