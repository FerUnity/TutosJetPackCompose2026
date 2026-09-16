package com.example.multitiendaapp.presentation.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitiendaapp.domain.repository.AuthRepository
import com.example.multitiendaapp.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//En la carpeta app iran los archivos que tienen que ver con la aplicacion en si,
// o sea con la globalidad de la app y no con las pantallas.

//Este viewmodel sera global de la app para det a que pantalla entrara el ususario cuando inicie la app.
//O sea si el usuario tiene una sesion activa lo enviara a la pantalla de home de seller o customer segun su rol.
//Y si no tiene sesion activa lo enviara a la pantalla de login.

@HiltViewModel //Para que este viewmodel pueda ser gestionado por hilt y pueda ser inyectado en otras clases de la app.
class AppViewModel @Inject constructor(
    private val authRepository: AuthRepository //Para que este viewmodel pueda acceder a los metodos de la interfaz AuthRepository,
//como saber si el usuario esta logueado o no, y que rol tiene, registrar un usuario, iniciar sesion, cerrar sesion, etc.
// con sus fun registerUser(), login(), getCurrentUser() y logout()// .
) : ViewModel() {
    //    Ahora creamos el estado de la app,
    //    Primero creamos el estado de la app en su version interna, que solo puede modificarse desde este viewmodel.
    //    Esta version del estado lo almacenamos en un objeto MutableStateFlow de tipo String?.
    private val _startDestination = MutableStateFlow<String?>(null)

    //    Luego creamos el estado de la app en su version externa, que es lo que se mostrara en la vista(UI),
//    y esta version del estado lo almacenamos en un objeto StateFlow de tipo String?.:
    val startDestination: StateFlow<String?> = _startDestination

//    Luego para que apenas se cree este viewmodel se verifique si el usuario esta logueado o no y que rol tiene,
//    llamamos a la fun checkSession() a traves de init:
    init {
        checkSession()
    }


    //Creamos una fun que verifique si el usuario esta logueado o no, y que rol:
    private fun checkSession() {
//        Lanzamos una corrutinba:
        viewModelScope.launch {
//            Primero llamamos a la fun que hace la consulta a la base de datos FIREBASE,
            //            para obtener al usuario actual desde la base de datos FIREBASE:
            val user = authRepository.getCurrentUser()
//            Luego si ese usuario actual existe, lo guardamos en el estado de la app
//            y lo enviamos segun su valor(_startDestination.value),
//            a la pantalla de home de seller o customer segun su rol, usamos when:
            _startDestination.value = when (user?.role) {
                "CUSTOMER" -> AppRoute.CustomerHome.route
                "SELLER" -> AppRoute.SellerHome.route
//               Ahora si no es seller o customer, lo enviamos a la pantalla de login:
                else -> AppRoute.Login.route

            }


        }
    }
}

