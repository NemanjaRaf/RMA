package com.nemanja02.rma.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nemanja02.rma.cats.db.CatDao
import com.nemanja02.rma.cats.db.CatData
import com.nemanja02.rma.users.db.UserDao
import com.nemanja02.rma.users.db.UserData

@Database(
    entities = [
        UserData::class,
        CatData::class,
    ],
    version = 5,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun catDao(): CatDao

//    abstract fun albumDao() : PhotosDao

}