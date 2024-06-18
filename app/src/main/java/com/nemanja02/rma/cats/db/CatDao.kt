package com.nemanja02.rma.cats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cat: CatData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cats: List<CatData>)

    @Query("SELECT * FROM CatData")
    suspend fun getAll(): List<CatData>

    @Transaction
    @Query("SELECT * FROM CatData")
    fun observeAll(): Flow<List<CatData>>

    @Query("SELECT * FROM CatData WHERE id = :id")
    fun observeCatById(id: String): Flow<CatData>

    // get random cat
    @Query("SELECT * FROM CatData ORDER BY RANDOM() LIMIT 1")
    fun getRandomCat(): CatData

    // get random n cats excluding the given id
    @Query("SELECT * FROM CatData WHERE id != :excludeId ORDER BY RANDOM() LIMIT :n")
    fun getRandomNCats(n: Int, excludeId: String): List<CatData>

    // getRandomTemperamentsExcluding(List<String> excludeTemperaments)
    @Query("SELECT DISTINCT temperament FROM CatData WHERE temperament NOT LIKE '%' || :excludeTemperaments || '%' ORDER BY RANDOM() LIMIT 2")
    fun getRandomTemperamentsExcluding(excludeTemperaments: List<String>): List<String>
}