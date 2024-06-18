package com.nemanja02.rma.navigation

import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nemanja02.rma.auth.AuthStore
import com.nemanja02.rma.cats.details.cat
import com.nemanja02.rma.cats.list.cats
import com.nemanja02.rma.leaderboard.leaderboard
import com.nemanja02.rma.quiz.quiz
import com.nemanja02.rma.users.profile.profile
import com.nemanja02.rma.users.register.model.asUserStore
import com.nemanja02.rma.users.register.register
import kotlinx.coroutines.launch

@Composable
fun ApplicationNavigation(authStore: AuthStore) {

    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val authData by authStore.authData.collectAsState(initial = null)

    println("AuthData = [$authData]")

    if (authData == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = if (authData!!.username == "") "register" else "cats",
            enterTransition = {
                slideInHorizontally(
                    animationSpec = spring(),
                    initialOffsetX = { it },
                )
            },
            exitTransition = { scaleOut(targetScale = 0.75f) },
            popEnterTransition = { scaleIn(initialScale = 0.75f) },
            popExitTransition = { slideOutHorizontally { it } },
        ) {
            cats(
                route = "cats",
                onCatClick = {
                    navController.navigate(route = "cats/$it")
                },
                onProfileClick = {
                    navController.navigate(route = "profile")
                },
                onCatsClick = {
                    navController.navigate(route = "cats")
                },
                onQuizClick = {
                    navController.navigate(route = "quiz")
                },
                onLeaderboardClick = {
                    navController.navigate(route = "leaderboard")
                },
                onSettingsClick = {
                    navController.navigate(route = "settings")
                },
                authData = authData
            )
            register(
                route = "register",
                onRegister = {
                    coroutineScope.launch {
                        println("Registering user...")
                        println(it)
                        authStore.updateAuthData(it.asUserStore())
                        navController.navigate("cats")
                    }
                }
            )
            profile(
                route = "profile",
                authData = authData,
                onCatsClick = {
                    navController.navigate(route = "cats")
                },
                onProfileClick = {
                    navController.navigate(route = "profile")
                },
                onQuizClick = {
                    navController.navigate(route = "quiz")
                },
                onLeaderboardClick = {
                    navController.navigate(route = "leaderboard")
                },
                onSettingsClick = {
                    navController.navigate(route = "settings")
                },
            )
            cat(
                route = "cats/{catID}",
                arguments = listOf(
                    navArgument(name = "catID") {
                        type = NavType.StringType
                        nullable = false
                    }
                ),
                onCatClick = {
                    navController.navigate(route = "cats/$it")
                },
                onProfileClick = {
                    navController.navigate(route = "profile")
                },
                onCatsClick = {
                    navController.navigate(route = "cats")
                },
                onQuizClick = {
                    navController.navigate(route = "quiz")
                },
                onLeaderboardClick = {
                    navController.navigate(route = "leaderboard")
                },
                onSettingsClick = {
                    navController.navigate(route = "settings")
                },
                authData = authData
            )

            quiz(
                route = "quiz",
                onCatClick = {
                    navController.navigate(route = "cats/$it")
                },
                onProfileClick = {
                    navController.navigate(route = "profile")
                },
                onCatsClick = {
                    navController.navigate(route = "cats")
                },
                onQuizClick = {
                    navController.navigate(route = "quiz")
                },
                onLeaderboardClick = {
                    navController.navigate(route = "leaderboard")
                },
                onSettingsClick = {
                    navController.navigate(route = "settings")
                },
                authData = authData,
                onQuizCompleted = {
                    navController.navigate(route = "cats")
                },
                onClose = {
                    navController.navigate(route = "cats")
                },
                onPublishScore = {
                    navController.navigate(route = "cats")
                }
            )

            leaderboard(
                route = "leaderboard",
                onCatClick = {
                    navController.navigate(route = "cats/$it")
                },
                onProfileClick = {
                    navController.navigate(route = "profile")
                },
                onCatsClick = {
                    navController.navigate(route = "cats")
                },
                onQuizClick = {
                    navController.navigate(route = "quiz")
                },
                onLeaderboardClick = {
                    navController.navigate(route = "leaderboard")
                },
                onSettingsClick = {
                    navController.navigate(route = "settings")
                },
                authData = authData

            )
        }
    }
}

inline val SavedStateHandle.catID: String
    get() = checkNotNull(get("catID")) { "catID is mandatory" }