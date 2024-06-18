package com.nemanja02.rma.users.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserData)

    @Query("SELECT * FROM UserData")
    suspend fun getAll(): List<UserData>

    // if one user is found, return it
    @Query("SELECT * FROM UserData LIMIT 1")
    suspend fun getUser(): UserData

}