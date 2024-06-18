package com.nemanja02.rma.users.profile

import com.nemanja02.rma.users.profile.model.UserUiModel
import com.nemanja02.rma.users.register.model.RegisterUiModel

data class ProfileState (
    val loading: Boolean = true,
    val updating: Boolean = false,
    val editing : Boolean = false,
    val userData: UserUiModel = UserUiModel(
        email = "",
        username = "",
        firstName = "",
        lastName = "",
    )
)
