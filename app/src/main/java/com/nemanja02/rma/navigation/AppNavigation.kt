package com.nemanja02.rma.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nemanja02.rma.cats.details.catDetails
import com.nemanja02.rma.cats.list.cats

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "cats") {
        cats(
            route = "cats",
            onCatSelected = {
                navController.navigate("cat/$it")
            }
        )

        catDetails(
            route = "cat/{catId}",
            arguments = listOf(
                navArgument(name = "catId") {
                    type = NavType.StringType
                    nullable = false
                }
            ),
            onClose = {
                navController.navigateUp()
            }
        )
    }
}