package com.sportsapp.data.teams.di

import android.content.Context
import androidx.room.Room
import com.sportsapp.data.teams.local.FavoriteTeamsDao
import com.sportsapp.data.teams.local.SportsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TeamDataModule {
    private const val DB_NAME = "sports_app.db"

    @Provides @Singleton
    fun provideSportsDatabase(@ApplicationContext context: Context): SportsDatabase =
        Room.databaseBuilder(context, SportsDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFavoriteTeamsDao(db: SportsDatabase): FavoriteTeamsDao = db.favoriteTeamsDao()
}