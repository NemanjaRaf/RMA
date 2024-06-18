package com.nemanja02.rma.drawer

sealed class AppDrawerDestination {
    data object Profile : AppDrawerDestination()
    data object Cats : AppDrawerDestination()
    data object Settings: AppDrawerDestination()
}