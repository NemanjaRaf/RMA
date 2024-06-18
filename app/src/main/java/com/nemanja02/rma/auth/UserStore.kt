package com.nemanja02.rma.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserStore(
    val first_name: String,
    val last_name: String,
    val username: String,
    val email: String,
)