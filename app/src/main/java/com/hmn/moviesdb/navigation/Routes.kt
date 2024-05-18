package com.hmn.moviesdb.navigation


sealed class Routes(val name: String, val title: String = "") {
    data object SplashScreen : Routes("splash_screen","Splash")
    data object LoginScreen : Routes("login_screen","Login")
    data object HomeScreen : Routes("home_screen","Home")
    data object DetailScreen : Routes("detail_screen","Detail")
    data object FavouriteScreen : Routes("favourite_screen","Favourite")
    data object SearchScreen : Routes("search_screen","Search")

}