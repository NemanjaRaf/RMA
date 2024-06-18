package com.nemanja02.rma.users.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemanja02.rma.auth.AuthStore
import com.nemanja02.rma.auth.UserStore
import com.nemanja02.rma.users.profile.model.UserUiModel
import com.nemanja02.rma.users.register.RegisterState
import com.nemanja02.rma.users.register.model.asUserStore
import com.nemanja02.rma.users.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository : UsersRepository,
    private val authStore: AuthStore
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    private fun setState(reducer: ProfileState.() -> ProfileState) = _state.update(reducer)

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

    fun setEditing(editing: Boolean) {
        setState { copy(editing = editing) }
    }

    fun UserStore.asUserUiModel() = UserUiModel(
        email = email,
        username = username,
        firstName = first_name,
        lastName = last_name
    )

    init {
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    val userData = authStore.authData.value
                    setState { copy(userData = userData.asUserUiModel()) }
                }
            } catch (error: Exception) {
                // TODO Handle error
                Log.e("Greska", "Message", error)
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

     fun saveUserData() {
        viewModelScope.launch {
            setState { copy(updating = true) }
            try {
                withContext(Dispatchers.IO) {
                    authStore.updateAuthData(state.value.userData.asUserStore())
                }
            } catch (error: Exception) {
                // TODO Handle error
                Log.e("Greska", "Message", error)
            } finally {
                setState { copy(updating = false) }
            }
        }
    }
}