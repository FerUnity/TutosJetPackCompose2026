package com.example.multitiendaapp.navigation

//La sgte clase sera para poder navegar entre pantallas junto con el la fun composable AppNavHost()
// Entonces aca definimos las pantallas que queremos navegar,
// lo que incluye ruta(route = "login") y nombre de la pantalla(data object).
sealed class AppRoute(val route: String) {
// Aca generamos las pantallas(data object) que queremos navegar y sus rutas(route) asi:
    // data object NombrePantalla : AppRoute("ruta Pantalla")

    data object Login : AppRoute("login")
//   En este caso la pantalla se llamara Login y su ruta "login".

    data object SelectRole : AppRoute("select_role")
//    La segunda pantalla se llamara SelectRole y la ruta sera "select_role".

    data object RegisterSeller : AppRoute("register_seller")
//    Pantalla de registro del vendedor.

    data object RegisterCustomer : AppRoute("register_customer")
//    Pantalla de registro del comprador.


}