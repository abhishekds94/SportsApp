package com.sportsapp.di

import com.sportsapp.data.teams.repository.TeamRepositoryImpl
import com.sportsapp.domain.teams.repository.TeamsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingsModule {
    @Binds
    @Singleton
    abstract fun bindTeamsRepository(
        impl: TeamRepositoryImpl
    ): TeamsRepository
}
