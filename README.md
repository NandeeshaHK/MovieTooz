# Movie App

A modern Android application built with Kotlin and Jetpack Compose for browsing movies.

## Features

*   **Browse Movies**: Discover popular, top-rated, and upcoming movies using the TMDB API.
*   **Favorites & Watchlist**: Save your favorite movies and create a watchlist using local database storage (Room).
*   **Material Design**: Beautiful UI implemented with Jetpack Compose and Material Design 3.
*   **Notifications**: Get notified about movie updates.

## Setup Instructions

1.  **Clone the Repository**
    ```bash
    git clone <repository-url>
    cd MovieApp
    ```

2.  **Open in Android Studio**
    - Launch Android Studio.
    - Select **Open** (or "Open an existing Android Studio project").
    - Navigate to the `MovieApp` directory and select it.
    - Determine that you trust the project if prompted.

3.  **Sync Project**
    - Android Studio should automatically detect the `build.gradle.kts` files.
    - If not, click **File > Sync Project with Gradle Files** or click the "Sync" icon (elephant) in the toolbar.
    - Wait for the Gradle sync to complete and dependencies to download.

## Steps to Run Locally

1.  **Configure Android Device**
    - Connect a physical Android device via USB (ensure USB Debugging is enabled).
    - OR: Create and start an Android Emulator (Avd) via Device Manager (Recommended: Pixel 6 or newer API 24+).

2.  **Build and Launch**
    - Select the `app` configuration in the toolbar.
    - Select your device/emulator.
    - Click the **Run** button (green play triangle) or press `Shift + F10`.
    - The compiled APK will be installed and launched on the device.

## Assumptions and Dependencies

### Assumptions
-   **API Key**: A TMDB API key is currently pre-configured in `app/build.gradle.kts` for testing purposes. No additional configuration is required to run the app immediately.
-   **Environment**: Development environment includes Android Studio (Hedgehog or newer recommended) and JDK 17+.
-   **Network**: An active internet connection is assumed for fetching movie data from the TMDB API.

### Dependencies & Tech Stack
This project relies on the following key libraries and tools:
-   **Language**: Kotlin
-   **UI Framework**: Jetpack Compose (BOM 2023.08.00)
-   **Architecture**: MVVM (Model-View-ViewModel)
-   **Dependency Injection**: Hilt (Dagger)
-   **Networking**: Retrofit 2 + GSON
-   **Image Loading**: Coil
-   **Local Database**: Room Database
-   **Asynchronous Programming**: Kotlin Coroutines & Flow
-   **Navigation**: Jetpack Navigation Compose
-   **Minimum SDK**: API 24 (Android 7.0)
-   **Target SDK**: API 34 (Android 14)

## Credits
Data provided by [The Movie Database (TMDB)](https://www.themoviedb.org/).
