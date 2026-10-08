package com.example.multitiendaapp.navigation

//La sgte clase sera para poder navegar entre pantallas junto con el la fun composable AppNavHost().

// Entonces aca definimos las pantallas por las que queremos navegar,
// lo que incluye el nombre de la pantalla(data object) y su ruta(route = "login").
// Esta ruta la llamaremos en la fun composable AppNavHost(),
// entonces no tenemos que usar en AppNavHost() el nombre string de la ruta,
// sino que usamos el nombre de la ruta definida aca:
sealed class AppRoute(val route: String) {
// Aca generamos las pantallas(data object) a las que queremos navegar,
    // con sus correspondientes rutas(route) asi:
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
    data object RegisterStore : AppRoute("register_store/{sellerUid}") {
        const val ARG_SELLER_UID = "sellerUid"

        //        Creamos una fun que nos permita crear la ruta de la pantalla de registro de tienda,
//        a partir de un sellerUid:
        fun createRoute(sellerUid: String): String {
            return "register_store/$sellerUid"
        }
    }

    //    Pantalla de inicio del cliente, luego de Registrarse o Iniciar sesion:
    data object CustomerHome : AppRoute("customer_home")

    //    Pantalla de inicio del vendedor, luego de Registrarse o Iniciar sesion:
    data object SellerHome : AppRoute("seller_home")

    //    Creamos 2 nuenas rutas que serviran como contenedores de navegacion que contendran las pantallas de inicio de vendedor y cliente,
//    luego de registrarse o iniciar sesion, para navegar entre pantallas:
    data object CustomerRoot : AppRoute("customer_root")

    data object SellerRoot : AppRoute("seller_root")

    //    Creamos 2 nuevas rutas para las tiendas:
    data object CustomerStores : AppRoute("customer_store")
//    Para que el cliente pueda ver las tiendas registradas de los vendedores.

    //    Creamos 2 rutas para listar categorias de las tiendas registradas y para ir al formulario de creacion y edicion de categorias:.
//    Ruta para mostrar una lista de las categorias de las tiendas registradas:
    data object SellerCategoriesList : AppRoute("seller/categories/list")

    data object SellerCategoryForm : AppRoute("seller/categories/form")


}