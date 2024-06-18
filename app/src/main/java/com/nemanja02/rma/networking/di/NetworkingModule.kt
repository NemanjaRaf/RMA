package com.nemanja02.rma.networking.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nemanja02.rma.networking.Serialization.AppJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val updatedRequest = it.request().newBuilder()
                    .addHeader("x-api-key", "live_vhKOYfTijo6jyD28YPXzjTJOrNg3M6ipPdWXuUnyB1Pg9fKFwVEtrC3TydPZaKwH")
                    .build()
                it.proceed(updatedRequest)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()
    }

    @Singleton
    @Provides
    @BaseUrl
    fun provideBaseUrl(): String {
        return "https://api.thecatapi.com/v1/"
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        @BaseUrl apiUrl: String,
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
