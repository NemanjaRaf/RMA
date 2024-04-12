package com.nemanja02.rma.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nemanja02.rma.networking.Serialization.AppJson
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val okHttpClient = OkHttpClient.Builder()
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


val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.thecatapi.com/v1/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()