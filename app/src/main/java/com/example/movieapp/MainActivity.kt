package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
                        
                        composable(
                            route = com.example.movieapp.ui.navigation.Screen.Home.route,
                            enterTransition = { fadeIn(tween(300)) },
                            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) + fadeOut(tween(300)) },
                            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) + fadeIn(tween(300)) }
                        ) {
                            com.example.movieapp.ui.screens.HomeScreen(
                                onMovieClick = { movieId ->
                                    navController.navigate(com.example.movieapp.ui.navigation.Screen.Details.createRoute(movieId))
                                }
                            )
                        }
                        
                        composable(
                            route = com.example.movieapp.ui.navigation.Screen.Details.route,
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
                            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) + fadeIn(tween(300)) },
                            exitTransition = { fadeOut(tween(300)) },
                            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) + fadeOut(tween(300)) }
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
