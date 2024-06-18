package com.nemanja02.rma.leaderboard.api.di

import com.nemanja02.rma.leaderboard.api.LeaderboardApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object LeaderboardModule {

    @Provides
    @Singleton
    fun provideLeaderboardApi(@com.nemanja02.rma.networking.di.LeaderboardApi retrofit: Retrofit): LeaderboardApi = retrofit.create()
}