package com.example.multitiendaapp.domain.repository

import com.example.multitiendaapp.core.model.AppUser
import com.example.multitiendaapp.core.model.UserRole

//En una interfaz solo se definen lo que hara la app por ejemplo:
// Es decir las fun para hacer login, register, logout, etc, con sus param y que devuelve,
// pero no los pasos logicos de como lo hara.
// por eso se dice que la interfaz es un contrato que se debe cumplir.
//La logica real y concreta se encuentra en la clase AuthRepoImplem.kt.

// Esta una interfaz para definir las operaciones (suspend fun) que realizaremos con la autenticacion del usuario:
interface AuthRepository {
//  Creamos una fun para regisrtrar un usuario: suspend significa que esta funcion puede ser pausada y reanudada
    //    sin bloquear el hilo principal de la aplicacion.,
    suspend fun registerUser(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    phone: String,
    role: UserRole //Aca se define el tipo de usuario que sera vendedor o cliente, segun la enum class UserRole
    ): Result<AppUser>
 //Aca se define que la funcion registerUser() devuelve un objeto de tipo Result<AppUser>(objeto Usuario),
    // o sea un nuevo tipo de Usuario,
// siempre que el registro de usuario sea exitoso.

//    Creamos una fun para iniciar sesion,
    //tambien suspendida porque requiere consultar a la base de datos FIREBASE y puede tardar un tiempo:
    suspend fun login(
    email: String,
    password: String): Result<AppUser>
//    Aca se define que la funcion loginUser() devuelve un nuevo Usuario Autenticado.

//    Creamos otra fun para obtener al usuario actual desde la base de datos FIREBASE,
//    que puede ser nula porque puede no existir:
    suspend fun getCurrentUser(): AppUser?

//    Creamos una fun para cerrar sesion:
    fun logout()
}
