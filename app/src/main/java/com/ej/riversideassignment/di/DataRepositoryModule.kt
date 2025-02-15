package com.ej.riversideassignment.di

import com.ej.riversideassignment.repositories.TitleRepository
import com.ej.riversideassignment.repositories.TitleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataRepositoryModule {
    @Binds
    abstract fun provideTitleRepository(impl: TitleRepositoryImpl): TitleRepository
}