package com.nemanja02.rma.db.di

import com.nemanja02.rma.db.AppDatabase
import com.nemanja02.rma.db.AppDatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

class DatabaseModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object DatabaseModule {

        @Singleton
        @Provides
        fun provideDatabase(builder: AppDatabaseBuilder): AppDatabase {
            return builder.build()
        }

    }
}