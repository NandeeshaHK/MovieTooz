package com.example.movieapp.ui.screens;

import androidx.lifecycle.SavedStateHandle;
import com.example.movieapp.data.repository.MovieRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class MovieDetailsViewModel_Factory implements Factory<MovieDetailsViewModel> {
  private final Provider<MovieRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public MovieDetailsViewModel_Factory(Provider<MovieRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public MovieDetailsViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static MovieDetailsViewModel_Factory create(Provider<MovieRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new MovieDetailsViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static MovieDetailsViewModel newInstance(MovieRepository repository,
      SavedStateHandle savedStateHandle) {
    return new MovieDetailsViewModel(repository, savedStateHandle);
  }
}
