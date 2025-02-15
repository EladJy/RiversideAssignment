package com.ej.riversideassignment.di

import android.content.Context
import androidx.room.Room
import com.ej.riversideassignment.db.TitleDetailsDao
import com.ej.riversideassignment.db.TitlesDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TitlesDb {
        return Room.databaseBuilder(context, TitlesDb::class.java, "titles_db")
            .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: TitlesDb): TitleDetailsDao {
        return database.titleDetailsDao()
    }
}