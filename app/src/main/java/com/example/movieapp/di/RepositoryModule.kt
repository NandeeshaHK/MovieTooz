package com.example.movieapp.di

import com.example.movieapp.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    // Repository is already @Singleton using @Inject constructor, 
    // but often we might want an interface. 
    // Since we are using the class directly for simplicity, 
    // we don't strictly need a module if we use @Inject on the constructor.
    // However, I will keep this module empty or remove it if I decided to inject interface later.
    // For now, I will rely on class injection.
}
