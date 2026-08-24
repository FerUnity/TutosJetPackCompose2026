package com.example.multitiendaapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitiendaapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ThreadLocalRandom.current
import javax.inject.Inject

//Este viewmodel es para la pantalla de login.

//Este data class repr el modelo de estado de la interfaz, para la pantalla de login:
data class UiState(
    val email: String = "",//repr el correo electronico que el usuario ingresa en el campo de texto de la pantalla de login.
    val password: String = "", //repr la contraseña que el usuario ingresa en el campo de texto de la pantalla de login.
    val isLoading: Boolean = false, //para controlar operaciones, ej que los botones esten deshabilitados mientras no se ingrese los datos.
    val errorMessage: String? = null //repr cualquier error que se presente en la pantalla de login.
)

//Creamos una sealed interface, que sera un conjto cerrado de eventos, que solo se decl en esta interfaz
//Los eventos son las acciones que el usuario realiza en la pantalla de login y que llegan a este viewmodel.
// Luego estos eventos se procesan mas abajo en la fun onEvent() del viewmodel.
sealed interface LoginEvent {
    //Indicamos el evento OnEmailChange, que repr cuando el usuario cambia el texto del campo de texto de correo electronico, en la pantalla de login.
    // El nuevo valor de correo electronico lo pasamos como parametro en la val value: String:
    data class OnEmailChange(val value: String) : LoginEvent
//    En la var value guardamos el nuevo valor de correo electronico que el usuario ingreso en el campo de texto de la pantalla de login.


    //  Indicamos el evento OnPasswordChange, que repr cuando el usuario cambia el texto del campo de texto de contraseña (actualiza la val password),
//  en la pantalla de login.
    data class OnPasswordChange(val value: String) : LoginEvent
//    En la var value guardamos el nuevo valor de contraseña que el usuario ingreso en el campo de texto de la pantalla de login.

    //  Indicamos el evento OnLoginClick, que repr cuando el usuario hace click en el boton de login.
//  Como no hay datos extra como los casos anteiores, no pasamos nada como parametro, solo usamos data object:
    data object OnLoginClick : LoginEvent

}

//Creamos otra sealed interface para los efectos colaterales de la pantalla de login.
// Los efectos son las acciones que emitimos desde el viewmodel a la vista(UI) y que solo se ejecutan 1 vez,
// como mostrar mensaje, permisos, nav entre pantallas:
sealed interface LoginEffect {
    data class ShowMessage(
        val message: String
    ) : LoginEffect //repr un mensaje que se muestra en la pantalla de login y que se emite desde el viewmodel a la vista(UI).

    data class NavigateByRole(
        val role: String
    ) : LoginEffect //repr el efecto que nos permitira navegar a cierta pantalla segun el rol del usuario.

}

@HiltViewModel //Indicamos que este viewmodel debe ser creado y gestionado por inyeccion de dependencias de Hilt.
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository //Inyectamos la dependencia del repositorio de autenticacion.
) : ViewModel() // Indicamos que esta clase extiende de ViewModel y que vivira mientras la vista que lo llame este viva.
{
    //    Creramos el estado internos y externos de la pantalla de login:
    //Primero creamos el estado de la pantalla login en su version interna, que sera mutable pero solo desde el viewmodel.
    // Y su estado inicial de cada campo es el del data class UiState(), ver arriba:
    private val _uiState = MutableStateFlow(UiState())

    //Luego creamos el estado del login en su version externa,
    // que estara expuesta a la vista(UI) para reaccionar a sus cambios y mostrarlos en la interfaz de usuario.
    // pero sera de solo lectura.
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    //   Ahora creamos el flujo de efectos colaterales de la pantalla de login, que sera mutable pero solo desde el viewmodel,
    //   para emitir efectos colaterales a la vista(UI) como mostrar mensajes, navegar entre pantallas y que solo se emitiran 1 vez:
    private val _effect = MutableSharedFlow<LoginEffect>()
    //flujo interno para emitir efx de una vez, como son mostrar mensajes y navegar

    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()
    // Esta es la version publica, o sea estara expuesta pero solo como lectura para coleccionarlas

//    Esta fun publica la creamos al final y es la puerta de entrada de la vista login (UI) para interactuar con el viewmodel.
//    O sea se evalua que tipo de evento se recibe y se procesa en la fun.
//    O sea si el usuario ingresa un correo o contraseña o hace click en el boton de login,
//    el evento se procesa en la fun onEvent() y se ejecuta una de las 3 fun correspondientes.
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> updateEmail(event.value) //Si hubo ingreso de email se envia ese evento a la fun updateEmail()
            is LoginEvent.OnPasswordChange -> updatePassword(event.value) //Si hubo ingreso de contraseña se envia ese evento a la fun updatePassword()
            is LoginEvent.OnLoginClick -> loginUser() //Si el usuario hace click en el boton de login se envia ese evento a la fun loginUser()
        }
    }

    //    Creamos funciones para actualizar el estado de la pantalla de login.
    //    Esto significa ene ste caso, que se actualiza el estado de los campos de texto del correo electronico y contraseña.
    //    Ademas una fun para manejar el evento de click en el boton de login que revisa los datos ingresados por el usuario:
    private fun updateEmail(value: String) {
        _uiState.update { current ->
            //hacemos una copia del estado actual y actualizamos el valor del campo de texto de correo electronico con el que ingreso el usuario.
            current.copy(
                email = value, //actualizamos el valor del campo de texto de correo electronico con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    private fun updatePassword(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto de contraseña con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                password = value, //actualizamos el valor del campo de texto de contraseña con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    //    Ahora creamos una fun que se encargara de manejar los eventos de la pantalla de login,
//    que contenga el flujo para iniciar sesion.
//    Es decir que cuando el usuario haga click en el boton de login,
//    se encargara de llamar a esta fun que va a verificar los datos del usuario y si son correctos.
//    Esta fun realiza una proteccion si se prersionea el boton de login varias veces:
    private fun loginUser() {
        if (_uiState.value.isLoading) return
        //Este if lo que es hace es verificar si el estado de la pantalla de login es cargando, si esta cargando, esta fun no hara nada.
        // o sea si el ususario presiona el boton de login varias veces, no hace nada porque ya esta cargando.

//    HArenos una corrutina sobre el ciclo de vida del viewmodel. Si el viewmodel muere, la corrutina tambien lo hara.
//    Sirve para ara operacoines asincronas como llamadas a Firebase:
        viewModelScope.launch {
            val emailTrimmed =
                //Primero con trim() eliminamos los espacios en blanco al inicio y al final del texto.
                _uiState.value.email.trim() //trim() elimina los espacios en blanco al inicio y al final del texto.
            val passwordTrimmed = _uiState.value.password.trim()
//            Verificamos que el correo no este vacio:
            if (emailTrimmed.isEmpty()) {
                _effect.emit(LoginEffect.ShowMessage("El correo electronico no puede estar vacio"))
                return@launch // Si el correo esta vacio se envia un mensaje y se sale de la fun loginUser() lo que hace que no se inicie sesion.
            }
//            Verificamos que la contraseña no este vacia:
            if (passwordTrimmed.isEmpty()) {
                _effect.emit(LoginEffect.ShowMessage("La contraseña no puede estar vacia"))
                return@launch // Si la contraseña esta vacia se envia un mensaje y se sale de la fun loginUser() lo que hace que no se inicie sesion.
            }

//            Actualizamos el estado visual de la pantalla de login para que se muestre que esta cargando:
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

//            Llamamos al repositorio de autenticacion Firebase para iniciar sesion,
//            y le pasamos el correo y la contraseña que ingreso el usuario.
//            Para eso creamos una variable llamada result que almacena el resultado de la consulta a Firebase:
            val result =
                authRepository.login(
                    //Si el login fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un usuario autenticado
                    //que tendra como datos el correo y la contraseña que ingreso el usuario, o sea emailTrimmed y passwordTrimmed.
                    email = emailTrimmed,
                    password = passwordTrimmed
                )
            //Si el login fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un usuario autenticado
            // y actulaizamos el estado de la pantalla de login:
            result.onSuccess { user ->
                _uiState.update { current ->
                    current.copy(
                        isLoading = false, // Deja de estar cargando la pantalla de login luego de iniciar sesion.
                        errorMessage = null // Borra el mensaje de error si lo habia
                    )
                }
//                Y llamamso a un efecto con un texto qe diga inicio de sesion exitoso:
                _effect.emit(LoginEffect.ShowMessage("Inicio de sesion exitoso"))

//                Y con otro efecto para entrar a la pantalla correspondiente al rol elegido por el usuario:
                _effect.emit(LoginEffect.NavigateByRole(user.role))

    //Si el login no fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un error.
                // y actulaizamos el estado de la pantalla de login:
            }.onFailure { error ->
                _uiState.update { current -> //Actualizamos el estado de la pantalla de login:
                    current.copy(
                        isLoading = false, // Deja de estar cargando la pantalla de login luego de iniciar sesion.
                        errorMessage = error.message ?: "No se pudo iniciar sesion"
                    // Y se muestra un mensaje de error que nos devuelve el repositorio de autenticacion Firebase y si no viene ponemos uno por defecto.
                    )
                }
                //Finalmente llamamos a un efecto con un texto que muestre mensaje de arriba o bien un mensaje de error por defecto:
                _effect.emit(LoginEffect.ShowMessage( error.message ?: "Inicio de sesion fallido"))
            }

        }
    }
}


