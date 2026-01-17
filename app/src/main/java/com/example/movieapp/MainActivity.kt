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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType

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
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = com.example.movieapp.ui.navigation.Screen.Home.route
                    ) {
                        
                        composable(com.example.movieapp.ui.navigation.Screen.Home.route) {
                            com.example.movieapp.ui.screens.HomeScreen(
                                onMovieClick = { movieId ->
                                    navController.navigate(com.example.movieapp.ui.navigation.Screen.Details.createRoute(movieId))
                                }
                            )
                        }
                        
                        composable(
                            route = com.example.movieapp.ui.navigation.Screen.Details.route,
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
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
