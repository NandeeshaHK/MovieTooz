package com.example.movieapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.ui.navigation.BottomNavItem

@Composable
fun HomeScreen(onMovieClick: (Int) -> Unit) {
    val navController = rememberNavController()
    
    val items = listOf(
        BottomNavItem.Movies,
        BottomNavItem.Favourites,
        BottomNavItem.Watchlist
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Movies.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Movies.route) {
                MoviesScreen(onMovieClick = onMovieClick)
            }
            composable(BottomNavItem.Favourites.route) {
                FavouritesScreen(onMovieClick = onMovieClick)
            }
            composable(BottomNavItem.Watchlist.route) {
                WatchlistScreen(onMovieClick = onMovieClick)
            }
        }
    }
}
