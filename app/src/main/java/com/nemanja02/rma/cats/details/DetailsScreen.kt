package com.nemanja02.rma.cats.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.nemanja02.rma.LocalAnalytics
import com.nemanja02.rma.auth.UserStore
import com.nemanja02.rma.core.compose.AppIconButton
import com.nemanja02.rma.core.theme.AppTheme
import com.nemanja02.rma.core.theme.EnableEdgeToEdge
import com.nemanja02.rma.drawer.AppDrawer
import com.nemanja02.rma.users.profile.ProfileScreen
import kotlinx.coroutines.launch

fun NavGraphBuilder.cat(
    route: String,
    arguments: List<NamedNavArgument>,
    onCatClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCatsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSettingsClick: () -> Unit,
    authData: UserStore?
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->

    val catDetailsViewModel: CatDetailsViewModel = hiltViewModel(navBackStackEntry)

    val state = catDetailsViewModel.state.collectAsState()

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
    state: CatDetailsState,
    onCatClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCatsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSettingsClick: () -> Unit,
    authData: UserStore? = null,
) {
    val uiScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)

    val analytics = LocalAnalytics.current
    SideEffect {
        analytics.log("Neka poruka")
    }

    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch { drawerState.close() }
    }

    AppDrawer(
        content = {
            CatListScaffold(
                state = state,
                onCatClick = onCatClick,
                onCatsClick = onCatsClick,
                onDrawerMenuClick = {
                    uiScope.launch {
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

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
private fun CatListScaffold(
    state: CatDetailsState,
    onCatClick: (String) -> Unit,
    onCatsClick: () -> Unit,
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
                    text = state.cat?.name ?: "Cat Details",
                    onTextLayout = {}
                ) },
                scrollBehavior = scrollBehavior,
                actions = {
                    AppIconButton(
                        imageVector = Icons.Default.CameraAlt,
                        onClick = onDrawerMenuClick,
                    )
                }
            )
        },
        content = { paddingValues ->
            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                state.cat?.let { cat ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(paddingValues)
                    ) {
                        // Cat Image
                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(cat.image?.url),
                                contentDescription = cat.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentScale = ContentScale.Crop,

                                )
                            IconButton(onClick = { onCatsClick() }) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Close")
                            }
                        }
                        Spacer(modifier = Modifier.padding(8.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Cat Name
                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Text(text = cat.name, style = MaterialTheme.typography.headlineMedium)
                                if (cat.rare == 1) {
                                    Badge(
                                        modifier = Modifier.padding(start = 8.dp),
                                        content = { Text("Rare", style = MaterialTheme.typography.bodyLarge) },
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.padding(4.dp))

                            // Origin Country and Flag
                            Row (verticalAlignment = Alignment.CenterVertically) {
                                println("Breed : ${cat.country_codes}")
                                Text(text = "Origin: ${cat.origin}", style = MaterialTheme.typography.bodyLarge)

                                Spacer(modifier = Modifier.padding(4.dp))

                                // Country Flag

                                // make countries list, split country_codes by comma
                                val countries = cat.country_codes.split(",")
                                println(countries)
                                for (country in countries) {
                                    val flagUrl = "https://flagsapi.com/${country}/flat/64.png"
                                    Image(
                                        painter = rememberAsyncImagePainter(flagUrl),
                                        contentDescription = "Flag of $country",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.padding(8.dp))

                            // Full Description
                            Text(text = cat.description, style = MaterialTheme.typography.bodyLarge)

                            Spacer(modifier = Modifier.padding(8.dp))

                            FlowRow(
//                                mainAxisSpacing = 8.dp, // Razmak između čipova u istom redu
//                                crossAxisSpacing = 8.dp // Razmak između redova
                                maxItemsInEachRow = 3
                            ) {
                                cat.temperament.split(",").forEach { temperament ->
                                    AssistChip(
                                        onClick = { /* Handle chip click */ },
                                        label = { Text(temperament.trim()) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.padding(8.dp))

                            // Life Span
                            Text(text = "Life Span: ${cat.life_span}", style = MaterialTheme.typography.bodyLarge)

                            Spacer(modifier = Modifier.padding(8.dp))

                            // Average Weight and Height
                            Text(text = "Weight: ${cat.weight?.metric} kg", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Height: ${cat.weight?.imperial} inches", style = MaterialTheme.typography.bodyLarge) // Pretpostavljam da je visina u polju "weight" greška, trebalo bi da bude u posebnom polju za visinu

                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(text = "Behavior and Needs", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.padding(8.dp))

                            Characteristic("Affection Level", cat.affection_level)
                            Spacer(modifier = Modifier.padding(2.dp))
                            Characteristic("Child Friendly", cat.child_friendly)
                            Spacer(modifier = Modifier.padding(2.dp))
                            Characteristic("Dog Friendly", cat.dog_friendly)
                            Spacer(modifier = Modifier.padding(2.dp))
                            Characteristic("Energy Level", cat.energy_level)
                            Spacer(modifier = Modifier.padding(2.dp))
                            Characteristic("Stranger Friendly", cat.stranger_friendly)

                            Spacer(modifier = Modifier.padding(8.dp))

                            Button(onClick = {

//                                eventPublisher(
//                                    CatDetailsContract.CatDetailsEvent.OpenWiki(cat.wikipedia_url?.toUri(), context)
//
//                                )
//                                val customTabsIntent = CustomTabsIntent.Builder().build()
//                                customTabsIntent.launchUrl(context, cat.wikipedia_url.toUri())

                            },
                                modifier = Modifier.fillMaxWidth()

                            ) {
                                Text(
                                    text = "Learn More on Wikipedia",
                                    overflow = TextOverflow.Ellipsis
                                )
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

@Composable
fun Characteristic(name: String, level: Int) {
    Column {
        Text(text = "$name: $level", style = MaterialTheme.typography.bodyLarge)
        LinearProgressIndicator(
            progress = level / 5f,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        Spacer(modifier = Modifier.padding(4.dp))
    }
}