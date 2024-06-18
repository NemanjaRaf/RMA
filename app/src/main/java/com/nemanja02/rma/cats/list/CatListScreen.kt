package com.nemanja02.rma.cats.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.nemanja02.rma.LocalAnalytics
import com.nemanja02.rma.auth.UserStore
import com.nemanja02.rma.core.compose.AppIconButton
import com.nemanja02.rma.core.theme.AppTheme
import com.nemanja02.rma.core.theme.EnableEdgeToEdge
import com.nemanja02.rma.drawer.AppDrawer
import kotlinx.coroutines.launch

fun NavGraphBuilder.cats(
    route: String,
    onCatClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCatsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSettingsClick: () -> Unit,
    authData: UserStore?
) = composable(
    route = route
) {
    val catListViewModel = hiltViewModel<CatListViewModel>()
    val state = catListViewModel.state.collectAsState()
    println("------------")
    println(state.value)
    EnableEdgeToEdge(isDarkTheme = false)
    CatListScreen(
        state = state.value,
        onCatClick = onCatClick,
        onProfileClick = onProfileClick,
        onCatsClick = onCatsClick,
        onQuizClick = onQuizClick,
        onLeaderboardClick = onLeaderboardClick,
        onSettingsClick = onSettingsClick,
        authData = authData
    )
}

@Composable
fun CatListScreen(
    state: CatListState,
    onCatClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCatsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSettingsClick: () -> Unit,
    authData: UserStore? = null,
) {
    val analytics = LocalAnalytics.current
    SideEffect {
        analytics.log("Neka poruka")
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppDrawer (
        content = {
            CatListScaffold(
                state = state,
                onCatClick = onCatClick,
                onDrawerMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        },
        drawerState = drawerState,
        onProfileClick = onProfileClick,
        onCatsClick = onCatsClick,
        onQuizClick = onQuizClick,
        onLeaderboardClick = onLeaderboardClick,
        onSettingsClick = onSettingsClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatListDrawer(
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
                        selected = false,
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
                        selected = true,
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
                        selected = false,
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
                        selected = false,
                        onClick = onLeaderboardClick,
                    )
                }

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                AppDrawerActionItem(
                    icon = Icons.Default.Settings,
                    text = "Settings",
                    onClick = onSettingsClick,
                )
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



@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CatListScaffold(
    state: CatListState,
    onCatClick: (String) -> Unit,
    onDrawerMenuClick: () -> Unit,
) {
    val uiScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.Menu,
                        onClick = onDrawerMenuClick,
                    )
                },
                title = { Text(
                    text = "Cats",
                    onTextLayout = {}
                ) },
                scrollBehavior = scrollBehavior,

            )
        },
        content = { paddingValues ->
            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxSize(),
                    state = listState,
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        items = state.cats,
                        key = { it.id },
                    ) { cat ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 16.dp)
                                .clickable { onCatClick(cat.id) },
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                cat.image?.let { image ->
                                    Image(
                                        painter = rememberAsyncImagePainter(image.url),
                                        contentDescription = cat.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp), // Set the height to maintain aspect ratio
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.padding(5.dp))
                                Text(
                                    text = cat.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    onTextLayout = {}
                                )
                                Spacer(modifier = Modifier.padding(5.dp))
                                Text(
                                    text = if (cat.description.length > 250) "${
                                        cat.description.take(
                                            250
                                        )
                                    }..." else cat.description,
                                    onTextLayout = {},
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    cat.temperament.split(",").shuffled().take(3)
                                        .forEach { temperament ->
                                            Badge() {
                                                Text(
                                                    text = temperament.trim(),
                                                    onTextLayout = {}
                                                )
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            }

        },
        floatingActionButton = {
            if (showScrollToTop) {
                FloatingActionButton(
                    onClick = {
                        uiScope.launch { listState.animateScrollToItem(index = 0) }
                    },
                ) {
                    Image(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                }
            }
        },
    )
}