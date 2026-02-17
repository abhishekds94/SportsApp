package com.sportsapp.data.teams.di

import com.sportsapp.data.teams.repository.TeamRepository
import com.sportsapp.data.teams.repository.TeamRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TeamDataModule {

    @Binds
    @Singleton
    abstract fun bindTeamRepository(
        impl: TeamRepositoryImpl
    ): TeamRepository
}