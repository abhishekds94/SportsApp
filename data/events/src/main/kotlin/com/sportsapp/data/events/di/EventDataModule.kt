package com.sportsapp.data.events.di

import com.sportsapp.data.events.repository.EventRepository
import com.sportsapp.data.events.repository.EventRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventDataModule {

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        impl: EventRepositoryImpl
    ): EventRepository
}