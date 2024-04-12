package com.nemanja02.rma.cats.api

import com.nemanja02.rma.cats.api.model.CatApiImage
import com.nemanja02.rma.cats.api.model.CatApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatsApi {

    @GET("breeds")
    suspend fun getBreeds(): List<CatApiModel>

    @GET("breeds/{id}")
    suspend fun getBreedById(
        @Path("id") id: String
    ): CatApiModel

    @GET("images/search")
    suspend fun getBreedImages(
        @Query("breed_id") id: String
    ): List<CatApiImage>
}