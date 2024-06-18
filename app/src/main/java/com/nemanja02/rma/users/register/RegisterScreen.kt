package com.nemanja02.rma.users.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nemanja02.rma.LocalAnalytics
import com.nemanja02.rma.core.showDialog
import com.nemanja02.rma.core.theme.EnableEdgeToEdge
import com.nemanja02.rma.users.profile.ProfileState
import com.nemanja02.rma.users.register.model.RegisterUiModel

fun NavGraphBuilder.register(
    route: String,
    onRegister: (RegisterUiModel) -> Unit
) = composable(
    route = route
) {
    val registerViewModel = hiltViewModel<RegisterViewModel>()
    val state = registerViewModel.state.collectAsState()
    EnableEdgeToEdge(isDarkTheme = false)
    RegisterScreen(
        state = state.value,
        onRegister = onRegister,
        registerViewModel = registerViewModel
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onRegister: (RegisterUiModel) -> Unit,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val uiScope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val analytics = LocalAnalytics.current
    SideEffect {
        analytics.log("Neka poruka")
    }


    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Register", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.userData.firstName,
                    onValueChange = { registerViewModel.updateFirstName(it) },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.userData.lastName,
                    onValueChange = { registerViewModel.updateLastName(it) },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.userData.email,
                    onValueChange = { registerViewModel.updateEmail(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.userData.username,
                    onValueChange = { registerViewModel.updateUsername(it) },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )

                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        println(state.userData)
                        if (state.userData.firstName.isEmpty() ||
                            state.userData.lastName.isEmpty() ||
                            state.userData.email.isEmpty() ||
                            state. userData.username.isEmpty()
                        ) {

                            showDialog(
                                context = context,
                                title = "Error",
                                message = "All fields are required",
                                showNegativeButton = false,
                                onClickListener = { _, _ -> }
                            )
                            return@Button
                        }
                        val registerData = RegisterUiModel(
                            firstName = state.userData.firstName,
                            lastName = state.userData.lastName,
                            email = state.userData.email,
                            username = state.userData.username,
                        )


                        registerViewModel.register(registerData)
                        onRegister(registerData)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        },
    )
}

