package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.movieapp.ui.theme.MovieAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = androidx.navigation.compose.rememberNavController()
                    
                    androidx.navigation.compose.NavHost(
                        navController = navController,
                        startDestination = com.example.movieapp.ui.navigation.Screen.Splash.route
                    ) {
                        androidx.navigation.compose.composable(com.example.movieapp.ui.navigation.Screen.Splash.route) {
                            com.example.movieapp.ui.screens.SplashScreen {
                                navController.navigate(com.example.movieapp.ui.navigation.Screen.Home.route) {
                                    popUpTo(com.example.movieapp.ui.navigation.Screen.Splash.route) { inclusive = true }
                                }
                            }
                        }
                        
                        androidx.navigation.compose.composable(com.example.movieapp.ui.navigation.Screen.Home.route) {
                            com.example.movieapp.ui.screens.HomeScreen(
                                onMovieClick = { movieId ->
                                    navController.navigate(com.example.movieapp.ui.navigation.Screen.Details.createRoute(movieId))
                                }
                            )
                        }
                        
                        androidx.navigation.compose.composable(
                            route = com.example.movieapp.ui.navigation.Screen.Details.route,
                            arguments = listOf(androidx.navigation.navArgument("movieId") { type = androidx.navigation.NavType.IntType })
                        ) {
                           com.example.movieapp.ui.screens.MovieDetailsScreen(
                               onBackClick = { navController.popBackStack() }
                           )
                        }
                    }
                }
            }
        }
    }
}
