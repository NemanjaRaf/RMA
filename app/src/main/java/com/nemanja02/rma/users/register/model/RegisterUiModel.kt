package com.nemanja02.rma.users.register.model

import com.nemanja02.rma.auth.UserStore
import com.nemanja02.rma.users.db.UserData
import com.nemanja02.rma.users.profile.model.UserUiModel

data class RegisterUiModel(
    var firstName: String,
    var lastName: String,
    var username: String,
    var email : String,
)

public fun RegisterUiModel.asUserStore() = UserStore(
    email = email,
    username = username,
    first_name = firstName,
    last_name = lastName,
)

public fun UserUiModel.asUserStore() = UserStore(
    email = email,
    username = username,
    first_name = firstName,
    last_name = lastName,
)



