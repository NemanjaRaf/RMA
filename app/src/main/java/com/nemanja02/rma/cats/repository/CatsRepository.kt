package com.nemanja02.rma.cats.repository

import com.nemanja02.rma.networking.retrofit
import com.nemanja02.rma.cats.api.CatsApi
import com.nemanja02.rma.cats.api.model.CatApiImage
import com.nemanja02.rma.cats.api.model.CatApiModel

object CatsRepository {
    private val catsApi: CatsApi = retrofit.create(CatsApi::class.java)

    suspend fun getBreeds(): List<CatApiModel> {
        val cats = catsApi.getBreeds()

        return cats
    }

    suspend fun getBreedById(id: String): CatApiModel {
        val cat = catsApi.getBreedById(id)

        return cat
    }

    suspend fun getBreedImages(id: String): CatApiImage? {
        val images = catsApi.getBreedImages(id).firstOrNull()

        return images
    }
}