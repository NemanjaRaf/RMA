package com.nemanja02.rma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.nemanja02.rma.analytics.AppAnalytics
import com.nemanja02.rma.auth.AuthStore
import com.nemanja02.rma.core.theme.AppTheme
import com.nemanja02.rma.navigation.ApplicationNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val analytics = AppAnalytics()
    @Inject
    lateinit var authStore: AuthStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {



            CompositionLocalProvider(
                LocalAnalytics provides analytics,
                LocalAuthStore provides authStore
            ) {
                AppTheme {

                    ApplicationNavigation(authStore)
                }
            }
        }
    }
}

val LocalAnalytics = compositionLocalOf<AppAnalytics> {
    error("Analytics not provided")
}

val LocalAuthStore = compositionLocalOf<AuthStore> {
    error("AuthStore not provided")
}