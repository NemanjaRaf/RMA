package com.nemanja02.rma.users.repository

import com.nemanja02.rma.db.AppDatabase
import com.nemanja02.rma.users.db.UserData
import javax.inject.Inject

class UsersRepository @Inject constructor(
//    private val usersApi: UsersApi,
    private val database: AppDatabase,
) {

    suspend fun registerUser(data: UserData) {
        database.userDao().insert(data)
    }

    suspend fun isUserRegistered(username: String): Boolean {
        val data = database.userDao().getUser()
        return data != null
    }

}
