package com.example.multitiendaapp.navigation

//La sgte clase sera para poder navegar entre pantallas junto con el la fun composable AppNavHost().

// Entonces aca definimos las pantallas por las que queremos navegar,
// lo que incluye el nombre de la pantalla(data object) y su ruta(route = "login").
// Esta ruta la llamaremos en la fun composable AppNavHost() :
sealed class AppRoute(val route: String) {
// Aca generamos las pantallas(data object) que queremos navegar y sus rutas(route) asi:
    //NOMBRE: data object NombrePantalla : RUTA: AppRoute("ruta Pantalla")

    //   En este caso la pantalla se llamara Login y su ruta "login":
    data object Login : AppRoute("login")


    //    La segunda pantalla se llamara SelectRole y la ruta sera "select_role":
    data object SelectRole : AppRoute("select_role")


    //    Pantalla de registro del vendedor.
    data object RegisterSeller : AppRoute("register_seller")


    //    Pantalla de registro del comprador.
    data object RegisterCustomer : AppRoute("register_customer")


//    Pantalla de registro de la tienda:
    data object RegisterStore : AppRoute("register_store")



}