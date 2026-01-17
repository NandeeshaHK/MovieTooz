package com.example.movieapp.data.repository;

import com.example.movieapp.data.local.MovieDao;
import com.example.movieapp.data.network.TmdbApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class MovieRepository_Factory implements Factory<MovieRepository> {
  private final Provider<TmdbApi> apiProvider;

  private final Provider<MovieDao> daoProvider;

  public MovieRepository_Factory(Provider<TmdbApi> apiProvider, Provider<MovieDao> daoProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
  }

  @Override
  public MovieRepository get() {
    return newInstance(apiProvider.get(), daoProvider.get());
  }

  public static MovieRepository_Factory create(Provider<TmdbApi> apiProvider,
      Provider<MovieDao> daoProvider) {
    return new MovieRepository_Factory(apiProvider, daoProvider);
  }

  public static MovieRepository newInstance(TmdbApi api, MovieDao dao) {
    return new MovieRepository(api, dao);
  }
}
