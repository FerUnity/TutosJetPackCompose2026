package com.example.swiperefresh.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewmodel : ViewModel() {
    //    Referenciamos el state de la clase HomeState para poder modificarlo
    //    en la vista y actualizarla en la UI con el valor actual:
    var state by mutableStateOf(HomeState())
        private set

    //    Creamos fun para actualizar el valor actual:
    fun updateCurrentValue() {
//        Primero activamos el swipe refresh por 2.5 segundos:
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            delay(2500)
            //        Luego actualizamos el valor actual:
            /*   state = state.copy(currentValue = state.currentValue + 1)
               //        Y desactivamos el swipe refresh:
               state = state.copy(isLoading = false)*/
//            Otra forma de hacer lo anterior:
            state = state.copy(
                currentValue = state.currentValue + 1,
                isLoading = false
            )
        }

    }
}
