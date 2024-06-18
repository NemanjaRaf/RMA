package com.nemanja02.rma.users.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemanja02.rma.auth.AuthStore
import com.nemanja02.rma.cats.api.model.CatApiImage
import com.nemanja02.rma.cats.api.model.CatApiWeight
import com.nemanja02.rma.cats.db.CatData
import com.nemanja02.rma.cats.list.CatListState
import com.nemanja02.rma.cats.model.CatUiModel
import com.nemanja02.rma.cats.model.Image
import com.nemanja02.rma.cats.model.Weight
import com.nemanja02.rma.cats.repository.CatsRepository
import com.nemanja02.rma.users.db.UserData
import com.nemanja02.rma.users.profile.model.UserUiModel
import com.nemanja02.rma.users.register.model.RegisterUiModel
import com.nemanja02.rma.users.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository : UsersRepository,
    private val authStore: AuthStore
) : ViewModel(){


    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()
    private fun setState(reducer: RegisterState.() -> RegisterState) = _state.update(reducer)

    fun updateEmail(email: String) {
        setState { copy(userData = userData.copy(email = email)) }
    }

    fun updateUsername(username: String) {
        setState { copy(userData = userData.copy(username = username)) }
    }

    fun updateFirstName(firstName: String) {
        setState { copy(userData = userData.copy(firstName = firstName)) }
    }

    fun updateLastName(lastName: String) {
        setState { copy(userData = userData.copy(lastName = lastName)) }
    }
    fun register(data: RegisterUiModel) {
        viewModelScope.launch {
            setState { copy(updating = true) }
            try {
                withContext(Dispatchers.IO) {
                    userRepository.registerUser(data.asUserData())
                }
            } catch (error: Exception) {
                // TODO Handle error
                Log.e("Greska", "Message", error)
            } finally {
                setState { copy(updating = false) }
            }
        }
    }

    private fun RegisterUiModel.asUserData() = UserData(
        email = email,
        username = username,
        first_name = firstName,
        last_name = lastName,
        id = 0
    )

    private fun UserData.asRegisterUiModel() = RegisterUiModel(
        email = email,
        username = username,
        firstName = first_name,
        lastName = last_name,
    )
}