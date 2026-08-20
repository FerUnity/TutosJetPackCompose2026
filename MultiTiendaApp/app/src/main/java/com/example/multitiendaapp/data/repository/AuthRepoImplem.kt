package com.example.multitiendaapp.data.repository

import com.example.multitiendaapp.core.model.AppUser
import com.example.multitiendaapp.core.model.UserRole
import com.example.multitiendaapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

//En esta clase es donde se implementara la logica real de las fun declaradas en la interfaz AuthRepository.kt
// respecto a la autenticacion del usuario con FireBase y Firestore.
//Entonces aca se implementan las fun declaradas en la interfaz AuthRepository.kt
class AuthRepoImplem(
//    Ahora hacemos la injeccion de dependencias con Firebase y Firestore(de AuthRepositoryModule) en el constructor de la clase AuthRepoImplem:
    private val auth: FirebaseAuth, //Para autenticar, reggistrar y cerrar sesion al usuario con FireBase
    private val firestore: FirebaseFirestore // Para acceder a la base de datos de FireBase
) : AuthRepository { //Aca se implementan las fun declaradas en la interfaz AuthRepository.kt:
    private val collectionUsers: String =
        "users" //Aca definimos la coleccion de usuarios en FireBase

    //    Creamos una fun que haga referencia a la coleccion de usuarios en FireBase,
//    para no tener que escribir firestore.collection() cada vez que se quiera acceder a la coleccion de usuarios:
    private fun usersRef() = firestore.collection(collectionUsers)

    //Cumplimos con el contrato de la interfaz AuthRepository.kt, implementando las fun de la interfaz:
    override suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        role: UserRole
    ): Result<AppUser> = runCatching {
        val firstNameTrimmed =
            firstName.trim() //trim para eliminar espacios en blanco al inicio y al final del nombre
        val lastNameTrimmed =
            lastName.trim() //trim para eliminar espacios en blanco al inicio y al final del apellido
        val emailTrimmed =
            email.trim() //trim para eliminar espacios en blanco al inicio y al final del correo
//La contrasweña no se hace el trim porque espacio puede ser contraseña:
// val passwordTrimmed = password.trim() //trim para eliminar espacios en blanco al inicio y al final de la contraseña


        val phoneTrimmed =
            phone.trim() //trim para eliminar espacios en blanco al inicio y al final del telefono

//    Validar que el nombre no este vacio despues del trim.
//    O sea aparecera un mensaje de error si el nombre esta vacio:
        if (firstNameTrimmed.isBlank()) {
            throw IllegalArgumentException("El nombre no puede estar vacio")
        }

        if (lastNameTrimmed.isBlank()) {
            throw IllegalArgumentException("El apellido no puede estar vacio")
        }

        if (emailTrimmed.isBlank()) {
            throw IllegalArgumentException("El correo no puede estar vacio")
        }

        if (password.isBlank()) {
            throw IllegalArgumentException("La contraseña no puede estar vacia")
        }

        if (phoneTrimmed.isBlank()) {
            throw IllegalArgumentException("El telefono no puede estar vacio")
        }

//        Fecha del dispositivo en miliseg:
        val createdAt = System.currentTimeMillis()

//        Registro del usuario en Forebase.
        //        Se requiere correo y pw para registrar al usuario:
        val authResult = auth.createUserWithEmailAndPassword(emailTrimmed, password).await()
        //await sirve para esperar a que se complete la tarea de registro en Firebase sin bloquear el hilo principal de la aplicacion.

//        Luego obtenemos al usuario registrado en Firebase.
//        Si no existe el usuario registrado en Firebase, se lanza una excepcion con el mensaje:
        val firebaseUser = authResult.user
            ?: throw IllegalStateException("No se pudo obtener al usuario registrado en Firebase Auth")
//        Usamos el operador elvis ?: para verificar que el usuario no sea nulo y si lo es, lanzar una excepcion.

//        Ahora generamos el AppUser final con los datos del usuario registrado en Firebase:
        val finalUser = AppUser(
            uid = firebaseUser.uid,
            firstName = firstNameTrimmed,
            lastName = lastNameTrimmed,
            email = emailTrimmed,
            phone = phoneTrimmed,
            role = role.name,
            createdAt = createdAt
        )

//        Crear un map para enviar los datos del usuario final, desde la App al FireStore:
        val data = mapOf(
            "uid" to finalUser.uid,
            "firstName" to finalUser.firstName,
            "lastName" to finalUser.lastName,
            "email" to finalUser.email,
            "phone" to finalUser.phone,
            "role" to finalUser.role,
            "createdAt" to finalUser.createdAt
        )

//Ahora obrenemos la ref a la coleccion de usuarios en FireStore:
        usersRef()
            .document(finalUser.uid).set(data).await()
        //await sirve para esperar a que se complete la tarea de registro en Firebase sin bloquear el hilo principal de la aplicacion.
        //Y finalmente retornamos el usuario final registrado en Firebase.
        finalUser
    }


    override suspend fun login(
        email: String,
        password: String
    ): Result<AppUser> = runCatching {
        val emailTrimmed = email.trim()

//    Validar que el correo no este vacio despues del trim.
//    Si el correo esta vacio se lanza una excepcion con un mensaje:
        if (emailTrimmed.isBlank()) {
            throw IllegalArgumentException("El correo no puede estar vacio")
        }

//    Tambien se valida que la contraseña no este vacia despues del trim:
        if (password.isBlank()) {
            throw IllegalArgumentException("La contraseña no puede estar vacia")
        }

//    Ahora iniciamos sesion con Firebase Auth, donde se requiere correo y pw para iniciar sesion:
        auth.signInWithEmailAndPassword(emailTrimmed, password).await()
        //await sirve para esperar a que se complete la tarea de registro en Firebase sin bloquear el hilo principal de la aplicacion.

//    Ahora obtenemos al usuario registrado y autenticado en Firebase:
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No se pudo obtener al usuario registrado en Firebase Auth")
//    Usamos el operador elvis ?: para verificar que el usuario no sea nulo y si lo es, lanzar una excepcion.

//    Consultamos a Firstore el documento del usuario registrado y autenticado y lo guardamos en la val snapshot:
        val snapshot = usersRef().document(currentUser.uid).get().await()
        //await sirve para esperar a que se obtengas la respuesta de Firestore, sin bloquear el hilo principal de la aplicacion.

//    Luego convertimos el documento obtenido de Firestore, en un objeto AppUser y lo almacenamos en la val appUser:
        val appUser = snapshot.toObject(AppUser::class.java)
            ?: throw IllegalStateException("No se pudo obtener la informacion del usuario registrado en Firestore")
//    Usamos el operador elvis ?: para verificar que el documento no sea nulo y si lo es, lanzar una excepcion.

//    Y finalmente retornamos el usuario autenticado y registrado en Firebase, ya convertido a AppUser:
        appUser


    }

    //    Lo que sigue ahora es implementar la fun para obtener al usuario actual desde la base de datos FIREBASE.
    //    Esta fun se creo en la interfaz AuthRepository.kt y se llama getCurrentUser().
    //    Se usa override porque se esta implementando la fun de la interfaz AuthRepository.kt:

    override suspend fun getCurrentUser(): AppUser? = runCatching {
        val currentUser = auth.currentUser ?: return null
//        llemos el doc del usuario actual desde Firestore, con su uid:
        val snapshot = usersRef().document(currentUser.uid).get().await()
//        Si el documento fue encontrado, lo convertimos en un objeto AppUser y lo retornamos:
        snapshot.toObject(AppUser::class.java)
    }.getOrNull() // Si la operacion falla, retornamos null en vez de lanzar una excepcion.
    //Esto para que no se caiga la app.


//    FinALMENTE creamos la fun para cerrar sesion:
    override fun logout() {
        auth.signOut()

    }
}