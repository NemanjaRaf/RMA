package com.nemanja02.rma.users.register

import com.nemanja02.rma.cats.model.CatUiModel
import com.nemanja02.rma.users.register.model.RegisterUiModel

data class RegisterState (
    val loading: Boolean = true,
    val updating: Boolean = false,
    val userData: RegisterUiModel = RegisterUiModel(
        email = "",
        username = "",
        firstName = "",
        lastName = "",
    )
)
