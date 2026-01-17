# Movie App

A modern Android application built with Kotlin and Jetpack Compose for browsing movies.

## Features

*   **Browse Movies**: Discover popular, top-rated, and upcoming movies using the TMDB API.
*   **Favorites & Watchlist**: Save your favorite movies and create a watchlist using local database storage (Room).
*   **Material Design**: Beautiful UI implemented with Jetpack Compose and Material Design 3.
*   **Notifications**: Get notified about movie updates.

## Tech Stack

*   **Language**: Kotlin
*   **UI Framework**: Jetpack Compose
*   **Architecture**: MVVM
*   **Network**: Retrofit (TMDB API)
*   **Database**: Room
*   **Dependency Injection**: Hilt (implied/recommended for modern Android apps, or manual DI if used)
*   **Async**: Coroutines & Flow

## Setup

1.  Clone the repository.
2.  Open in Android Studio.
3.  Add your TMDB API key in `local.properties` or code configuration.
4.  Build and run the app.

## Credits

Data provided by [The Movie Database (TMDB)](https://www.themoviedb.org/).
