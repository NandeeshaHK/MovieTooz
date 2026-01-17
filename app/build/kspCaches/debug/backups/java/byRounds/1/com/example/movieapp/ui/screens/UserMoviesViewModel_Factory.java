package com.example.movieapp.ui.screens;

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
public final class UserMoviesViewModel_Factory implements Factory<UserMoviesViewModel> {
  private final Provider<MovieRepository> repositoryProvider;

  public UserMoviesViewModel_Factory(Provider<MovieRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UserMoviesViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static UserMoviesViewModel_Factory create(Provider<MovieRepository> repositoryProvider) {
    return new UserMoviesViewModel_Factory(repositoryProvider);
  }

  public static UserMoviesViewModel newInstance(MovieRepository repository) {
    return new UserMoviesViewModel(repository);
  }
}
