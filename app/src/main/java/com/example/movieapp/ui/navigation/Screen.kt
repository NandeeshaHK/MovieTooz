package com.example.movieapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: Int) = "details/$movieId"
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Movies : BottomNavItem("movies", "Movies", Icons.Default.Movie)
    object Favourites : BottomNavItem("favourites", "Favourites", Icons.Default.Favorite)
    object Watchlist : BottomNavItem("watchlist", "Watchlist", Icons.AutoMirrored.Filled.List)
}
