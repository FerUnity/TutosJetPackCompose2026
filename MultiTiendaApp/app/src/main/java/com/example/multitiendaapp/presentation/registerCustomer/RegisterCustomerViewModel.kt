package com.example.multitiendaapp.presentation.registerCustomer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitiendaapp.core.model.UserRole
import com.example.multitiendaapp.domain.repository.AuthRepository
import com.example.multitiendaapp.presentation.login.LoginEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//Este viewmodel es para la pantalla de registro de cliente.

//Creamos un data class UiState, que es una clase de datos
// que se usa para representar el estado de la pantalla de registro de cliente e ir actualizandolo:
data class UiState(
    val firstName: String = "", //repr el nombre que el usuario ingresa en el campo de texto de la pantalla de registro de cliente.
    val lastName: String = "", //repr el apellido que el usuario ingresa en el campo de texto de la pantalla de registro de cliente.
    val email: String = "", // repr el correo electronico que el usuario ingresa en el campo de texto de la pantalla de registro de cliente.
    val password: String = "", //repr la contraseña que el usuario ingresa en el campo de texto de la pantalla de registro de cliente.
    val confirmPassword: String = "", //repr la confirmacion de la contraseña que el usuario ingresa en el campo de texto de la pantalla de registro de cliente.
    val isLoading: Boolean = false,
    //para saber si el registro esta cargando o no, para controlar operaciones, ej que los botones esten deshabilitados mientras no se ingrese los datos.
    val errorMessage: String? = null //repr cualquier error que se presente en la pantalla de registro de cliente.
)

//Creamos un sealed interface para los eventos de la pantalla de registro de cliente.
// Los eventos son las acciones que el usuario realiza en la pantalla de registro de cliente y que llegan a este viewmodel.
// Luego estos eventos se procesan mas abajo en la fun onEvent() del viewmodel.

sealed interface CustomerEvent {
    data class OnFirstNameChange(val value: String) : CustomerEvent
//    En la var value guardamos el nuevo valor de nombre que el usuario ingreso(evento) en el campo de texto de la pantalla de registro de cliente.

    data class OnLastNameChange(val value: String) : CustomerEvent
//    En la var value guardamos el nuevo valor de apellido que el usuario ingreso en el campo de texto de la pantalla de registro de cliente.

    data class OnEmailChange(val value: String) : CustomerEvent
//    En la var value guardamos el nuevo valor de correo electronico que el usuario ingreso en el campo de texto de la pantalla de registro de cliente.

    data class OnPasswordChange(val value: String) : CustomerEvent
//    En la var value guardamos el nuevo valor de contraseña que el usuario ingreso en el campo de texto de la pantalla de registro de cliente.

    data class OnConfirmPasswordChange(val value: String) : CustomerEvent
//    En la var value guardamos el nuevo valor de confirmacion de contraseña que el usuario ingreso en el campo de texto de la pantalla de registro de cliente.

    data object OnRegisterClick : CustomerEvent
//    Indicamos el evento OnRegisterClick, que repr cuando el usuario hace click en el boton de registro.
//    Como no hay datos extra como los casos anteiores, no pasamos nada como parametro, solo usamos data object:

}

//Creamos otra sealed interface para los efectos colaterales de la pantalla de registro de cliente.
// Los efectos son las acciones que emitimos desde el viewmodel a la vista(UI) y que solo se ejecutan 1 vez,
// como mostrar mensaje, permisos, nav entre pantallas:

sealed interface CustomerEffect {
    data class ShowMessage(
        val message: String
    ) : CustomerEffect //repr un mensaje que se muestra en la pantalla de registro de cliente y que se emite desde el viewmodel a la vista(UI).

    data object NavigateToHome : CustomerEffect
//repr el efecto que nos permitira navegar a la pantalla inicial de la aplicacion, pero como cliente.

}

@HiltViewModel //Indicamos que este viewmodel debe ser creado y gestionado por inyeccion de dependencias de Hilt.
class RegisterCustomerViewModel @Inject constructor(
    private val authRepository: AuthRepository
//Inyectamos la dependencia del repositorio de autenticacion, para registro de cliente en la base de datos FireStore.
) : ViewModel() // Indicamos que esta clase extiende de ViewModel y que vivira mientras la vista que lo llame este viva.
{
//    Crewamos el estado interno y externo de la pantalla de registro de cliente(UiState()):
//    //Primero creamos el estado de la pantalla de registro de cliente en su version interna,
    //    que sera mutable pero solo desde el viewmodel.
//    // Y su estado inicial de cada campo es el del data class UiState(), ver arriba:

    private val _uiState = MutableStateFlow(UiState())

    //Luego creamos el estado del login en su version externa,
    // que estara expuesta a la vista(UI) para reaccionar a sus cambios y mostrarlos en la interfaz de usuario.
    // pero sera de solo lectura:
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    //   Ahora creamos el flujo de efectos colaterales de la pantalla de login, que sera mutable pero solo desde el viewmodel,
    //   para emitir efectos colaterales a la vista(UI) como mostrar mensajes, navegar entre pantallas y que solo se emitiran 1 vez:
    private val _effect = MutableSharedFlow<CustomerEffect>()
    //flujo interno para emitir efx de una vez, como son mostrar mensajes y navegar

    val effect: SharedFlow<CustomerEffect> = _effect.asSharedFlow()
    // Esta es la version publica, o sea estara expuesta pero solo como lectura para coleccionarlas


    //    Esta fun publica la creamos al final y es la puerta de entrada de la vista RegisterCustomerScreen(UI) para interactuar con el viewmodel.
//    O sea se evalua que tipo de evento se recibe y se procesa en la fun correspondiente.
//    o sea si el usuario ingresa un nombre o apellido o correo o contraseña o hace click en el boton de registro,
//    el evento se procesa en la fun onEvent() y se ejecuta una de las 6 fun correspondientes.
    fun onEvent(event: CustomerEvent) {
        when (event) {
            is CustomerEvent.OnFirstNameChange -> updateFirstName(event.value) //Si hubo ingreso de nombre se envia ese evento a la fun updateFirstName()
            is CustomerEvent.OnLastNameChange -> updateLastName(event.value) //Si hubo ingreso de apellido se envia ese evento a la fun updateLastName()
            is CustomerEvent.OnEmailChange -> updateEmail(event.value) //Si hubo ingreso de correo se envia ese evento a la fun updateEmail()
            is CustomerEvent.OnPasswordChange -> updatePassword(event.value) //Si hubo ingreso de contraseña se envia ese evento a la fun updatePassword()
            is CustomerEvent.OnConfirmPasswordChange -> updateConfirmPassword(event.value) //Si hubo ingreso de confirmacion de contraseña se envia ese evento a la fun updateConfirmPassword()
            is CustomerEvent.OnRegisterClick -> registerCustomer() //Si el usuario hace click en el boton de registro se envia ese evento a la fun registerCustomer()
        }
    }


    //    Ahora creamos las fun para actualizar el estado de la pantalla de registro de cliente.
//    Esto significa ene ste caso, que se actualiza el estado de los campos de texto del nombre, apellido, correo electronico, contraseña y confirmacion de contraseña.
//    Ademas una fun para manejar el evento de click en el boton de registro que revisa los datos ingresados por el usuario:
    private fun updateFirstName(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto del nombre con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                firstName = value, //actualizamos el valor del campo de texto del nombre con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    //    Ahora cxreamos la fun actualizar el estado de la pantalla de registro de cliente, para el apellido:
    private fun updateLastName(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto del apellido con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                lastName = value, //actualizamos el valor del campo de texto del apellido con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    // Creamos la fun para actualizar el estado de la pantalla de registro de cliente, para el correo electronico:
    private fun updateEmail(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto del correo electronico con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                email = value, //actualizamos el valor del campo de texto del correo electronico con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    //    Creamos la fun para actualizar el estado de la pantalla de registro de cliente, para la contraseña:
    private fun updatePassword(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto de la contraseña con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                password = value, //actualizamos el valor del campo de texto de la contraseña con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }


    }

    //    Creamos la fun para actualizar el estado de la pantalla de registro de cliente, para la confirmacion de contraseña:
    private fun updateConfirmPassword(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto de la confirmacion de contraseña con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                confirmPassword = value, //actualizamos el valor del campo de texto de la confirmacion de contraseña con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }


    //    Ahora creamos una fun que se encargara de manejar los eventos de la pantalla de registro de cliente,
//    que contenga el flujo para registrar el cliente.
//    Es decir que cuando el usuario haga click en el boton de registro, se encargara de llamar a esta fun,
//    que va a verificar los datos del usuario y si son correctos.
    //    Ademas esta fun realiza una proteccion si se prersionea el boton de registro varias veces:

    private fun registerCustomer() {
        if (_uiState.value.isLoading) return
        //Este if lo que es hace es verificar si el estado de la pantalla de registro de cliente es cargando, si esta cargando, esta fun no hara nada.
        // o sea si el ususario presiona el boton de registro varias veces, no hace nada porque ya esta cargando.

//  //    HArenos una corrutina sobre el ciclo de vida del viewmodel. Si el viewmodel muere, la corrutina tambien lo hara.
        //  Servira para operaciones asincronas:
        viewModelScope.launch {
            //Primero con trim() eliminamos los espacios en blanco al inicio y al final del texto.
            val firstNameTrimmed = _uiState.value.firstName.trim()
            val lastNameTrimmed = _uiState.value.lastName.trim()
            val emailTrimmed = _uiState.value.email.trim()
            val passwordTrimmed = _uiState.value.password.trim()
            val confirmPasswordTrimmed = _uiState.value.confirmPassword.trim()

//            Ahora verificamos que los campos no esten vacios:
            if (firstNameTrimmed.isEmpty()) {
                _effect.emit(CustomerEffect.ShowMessage("El nombre no puede estar vacio"))
                return@launch
                // Si el nombre esta vacio se envia el mensaje y se sale de la fun registerCustomer() lo que hace que no se haga el registro.
            }
            if (lastNameTrimmed.isEmpty()) {
                _effect.emit(CustomerEffect.ShowMessage("El apellido no puede estar vacio"))
                return@launch
            }
            if (emailTrimmed.isEmpty()) {
                _effect.emit(CustomerEffect.ShowMessage("El correo electronico no puede estar vacio"))
                return@launch
            }
            if (passwordTrimmed.isEmpty()) {
                _effect.emit(CustomerEffect.ShowMessage("La contraseña no puede estar vacia"))
                return@launch
            }
            if (confirmPasswordTrimmed.isEmpty()) {
                _effect.emit(CustomerEffect.ShowMessage("La confirmacion de contraseña no puede estar vacia"))
                return@launch
            }

//            Condicion que la contraseña y su confirmacion debe ser iiguales:
            if (passwordTrimmed != confirmPasswordTrimmed) {
                _effect.emit(CustomerEffect.ShowMessage("Las contraseñas no coinciden"))
                return@launch
            }

            //Ahora actualizamos el estado visual de la pantalla de registro de cliente para que se muestre que esta cargando:
            _uiState.update { current ->
//            Obtenemos el estadfo actual para hacer una copia
                // y mostrar que esta cargando la pantalla de registro de cliente.
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            //Llamamos al repositorio de autenticacion Firebase para registrar al cliente,
            // y le pasamos el nombre, apellido, correo y contraseña que ingreso el usuario.
            // Para eso creamos una variable llamada result que almacena el resultado de la consulta a Firebase:
            val result =
//     Si el registro fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un usuario autenticado.
            // que tendra como datos el nombre, apellido, correo y contraseña que ingreso el usuario, osea
                // firstNameTrimmed, lastNameTrimmed, emailTrimmed y passwordTrimmed.
                authRepository.registerUser(
                    firstName = firstNameTrimmed,
                    lastName = lastNameTrimmed,
                    email = emailTrimmed,
                    password = passwordTrimmed,
                    phone = "",
                    role = UserRole.CUSTOMER
                )

//            Si el registro fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un usuario autenticado.
//            // y actulaizamos el estado de la pantalla de registro de cliente:
            result.onSuccess { user ->
                _uiState.update { current ->
                    //Actualizamos el estado de la pantalla de registro de cliente, resetado a su estado inicial:
                    current.copy(
                        firstName = "",
                        lastName = "",
                        email = "",
                        password = "",
                        confirmPassword = "",
                        isLoading = false, // Deja de estar cargando la pantalla de registro de cliente luego de registrarse.
                        errorMessage = null // Borra el mensaje de error si lo habia
                    )
                }

//                Agregamos un efecto para mostrar un mensaje de registro exitoso:
                _effect.emit(CustomerEffect.ShowMessage("Registro exitoso"))

//                Agregamos un efecto para navegar a la pantalla inicial de la aplicacion, pero como cliente:
                _effect.emit(CustomerEffect.NavigateToHome)
            }
//            Si el, proceso de registro no fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un error:
                .onFailure { error ->
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false, // Deja de estar cargando la pantalla de registro de cliente luego de registrarse.
                            errorMessage = error.message ?: "No se pudo registrar"
                            // Y se muestra un mensaje de error que nos devuelve el repositorio de autenticacion Firebase y si no viene ponemos uno por defecto.
                        )

                    }

                    //Finalmente llamamos a un efecto con un texto que muestre mensaje de arriba o bien un mensaje de error por defecto:
                    _effect.emit(CustomerEffect.ShowMessage(error.message ?: "Registro fallido"))


//
                }
        }
    }
}

