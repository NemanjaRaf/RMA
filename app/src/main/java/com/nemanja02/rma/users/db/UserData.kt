package com.nemanja02.rma.users.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData(
    @PrimaryKey val id: Int,
    val first_name: String,
    val last_name: String,
    val username: String,
    val email: String,
)