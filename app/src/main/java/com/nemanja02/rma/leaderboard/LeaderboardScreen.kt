package com.nemanja02.rma.leaderboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nemanja02.rma.LocalAnalytics
import com.nemanja02.rma.auth.UserStore
import com.nemanja02.rma.core.compose.AppIconButton
import com.nemanja02.rma.core.theme.EnableEdgeToEdge
import com.nemanja02.rma.drawer.AppDrawer
import kotlinx.coroutines.launch
import java.util.Date

fun NavGraphBuilder.leaderboard(
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
    val leaderboardViewModel = hiltViewModel<LeaderboardViewModel>()
    val state = leaderboardViewModel.state.collectAsState()
    println("------------")
    println(state.value)
    EnableEdgeToEdge(isDarkTheme = false)
    LeaderboardScreen(
        state = state.value,
        onProfileClick = onProfileClick,
        onCatsClick = onCatsClick,
        onQuizClick = onQuizClick,
        onLeaderboardClick = onLeaderboardClick,
        onSettingsClick = onSettingsClick,
        authData = authData
    )
}

@Composable
fun LeaderboardScreen(
    state: LeaderboardState,
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
            LeaderboardScaffold(
                state = state,
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
        selected = 3
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LeaderboardScaffold(
    state: LeaderboardState,
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
                    text = "Leaderboard",
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
                    itemsIndexed(
                        items = state.rankings,
                        key = { index, item -> item.createdAt }
                    ) { index, cat ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp), // Add padding if needed
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically // Center vertically if needed
                                ) {
                                    Text(
                                        text = "${index+1}. ${cat.nickname}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp)) // Adjust space as needed
                                    Text(
                                        text = cat.result.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.End // Align text to the end (right)
                                    )
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

private fun longToDate(time: Long): String {
    // format date dd.MM.yyyy

    val date = Date(time)
    return date.toString()
}