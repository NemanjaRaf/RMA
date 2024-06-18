package com.nemanja02.rma.drawer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.nemanja02.rma.LocalAuthStore
import com.nemanja02.rma.auth.UserStore
import com.nemanja02.rma.core.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    content: @Composable () -> Unit,
    drawerState: DrawerState,
    onProfileClick: () -> Unit,
    onCatsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSettingsClick: () -> Unit,
    selected: Int = -1,
): Pair<() -> Unit, () -> Unit> {
    val authStore = LocalAuthStore.current
    val navController = rememberNavController()
    val uiScope = rememberCoroutineScope()
    val authData by authStore.authData.collectAsState(initial = null)

    val openDrawer: () -> Unit = {
        println("Open drawer")
        uiScope.launch {
            drawerState.open()
        }
    }

    val closeDrawer: () -> Unit = {
        uiScope.launch {
            drawerState.close()
        }
    }

    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        drawerContent = {
            CatListDrawer(
                selected = selected,
                onProfileClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onProfileClick()
                },
                onCatsClick = {
                    uiScope.launch { drawerState.close() }
                    onCatsClick()
                },
                onQuizClick = {
                    uiScope.launch { drawerState.close() }
                    onQuizClick()
                },
                onLeaderboardClick = {
                    uiScope.launch { drawerState.close() }
                    onLeaderboardClick()
                },
                onSettingsClick = {
                    uiScope.launch { drawerState.close() }
                    onSettingsClick()
                },

                authData = authData
            )
        },
        content = content
    )

    return openDrawer to closeDrawer
}

@Composable
private fun CatListDrawer(
    selected: Int = -1,
    onProfileClick: () -> Unit,
    onCatsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSettingsClick: () -> Unit,
    authData: UserStore? = null,
) {
    BoxWithConstraints {
        // We can use ModalDrawerSheet as a convenience or
        // built our own drawer as AppDrawer example
        ModalDrawerSheet(
            modifier = Modifier.width(maxWidth * 3 / 4),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.BottomStart,
                ) {
                    Text(
                        modifier = Modifier.padding(all = 16.dp),
                        text = authData?.username ?: "Guest",
                        style = AppTheme.typography.headlineSmall,
                        onTextLayout = {}
                    )
                }

                Column(modifier = Modifier.weight(1f)) {

                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Profile",
                                onTextLayout = {}
                            )
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        selected = selected == 0,
                        onClick = onProfileClick
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Cats",
                                onTextLayout = {}
                            )
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Pets, contentDescription = null)
                        },
                        selected = selected == 1,
                        onClick = onCatsClick
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Quiz",
                                onTextLayout = {}
                            )
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Quiz, contentDescription = null)
                        },
                        selected = selected == 2,
                        onClick = onQuizClick,
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Leaderboard",
                                onTextLayout = {}
                            )
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Leaderboard, contentDescription = null)
                        },
                        selected = selected == 3,
                        onClick = onLeaderboardClick,
                    )
                }

//                HorizontalDivider(modifier = Modifier.fillMaxWidth())
//
//                AppDrawerActionItem(
//                    icon = Icons.Default.Settings,
//                    text = "Settings",
//                    onClick = onSettingsClick,
//                )
            }
        }
    }
}

@Composable
private fun AppDrawerActionItem(
    icon: ImageVector,
    text: String,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = Modifier.clickable(
            enabled = onClick != null,
            onClick = { onClick?.invoke() }
        ),
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        headlineContent = {
            Text(
                text = text,
                onTextLayout = {}
            )
        }
    )
}