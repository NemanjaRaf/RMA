package com.nemanja02.rma.cats.repository

import com.nemanja02.rma.cats.api.CatsApi
import com.nemanja02.rma.cats.mappers.asCatDbModel
import com.nemanja02.rma.db.AppDatabase
import javax.inject.Inject

class CatsRepository @Inject constructor(
    private val catsApi: CatsApi,
    private val database: AppDatabase,
) {
    suspend fun fetchAll() {
        val breeds = catsApi.getBreeds()
        database.catDao().insertAll(cats = breeds.map { it.asCatDbModel() })
    }
    fun observeAllCats() = database.catDao().observeAll()

    fun fetchCatById(id: String) = database.catDao().observeCatById(id)

    fun getRandomCat() = database.catDao().getRandomCat()

    fun getRandomNCats(n: Int, excludeId: String) = database.catDao().getRandomNCats(n, excludeId)

    fun getRandomTemperamentsExcluding(excludeTemperaments: List<String>) = database.catDao().getRandomTemperamentsExcluding(excludeTemperaments)
}