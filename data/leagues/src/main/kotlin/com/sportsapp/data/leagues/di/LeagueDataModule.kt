package com.sportsapp.data.leagues.di

import com.sportsapp.data.leagues.repository.LeagueRepository
import com.sportsapp.data.leagues.repository.LeagueRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LeagueDataModule {

    @Binds
    @Singleton
    abstract fun bindLeagueRepository(
        impl: LeagueRepositoryImpl
    ): LeagueRepository
}