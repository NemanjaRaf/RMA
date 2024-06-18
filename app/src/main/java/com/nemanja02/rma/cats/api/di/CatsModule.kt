package com.nemanja02.rma.cats.api.di

import com.nemanja02.rma.cats.api.CatsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsersModule {

    @Provides
    @Singleton
    fun provideCatsApi(retrofit: Retrofit): CatsApi = retrofit.create()
}