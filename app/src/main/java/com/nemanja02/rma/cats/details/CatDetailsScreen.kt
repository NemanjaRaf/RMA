package com.nemanja02.rma.cats.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow

fun NavGraphBuilder.catDetails(
    route: String,
    arguments: List<NamedNavArgument>,
    onClose: () -> Unit
) = composable(route, arguments = arguments) { navBackStackEntry ->
    val catId = navBackStackEntry.arguments?.getString("catId")
        ?: throw IllegalStateException("catId required")

    var catDetailsViewModel = viewModel<CatDetailsViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatDetailsViewModel(catId = catId) as T
            }
        }
    )

    val state = catDetailsViewModel.state.collectAsState()

    CatDetailsScreen(
        state = state.value,
        eventPublisher = {
            catDetailsViewModel.setEvent(it)
        },
        onClose = onClose,
    )

}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CatDetailsScreen(
    state: CatDetailsContract.CatDetailsState,
    eventPublisher: (CatDetailsContract.CatDetailsEvent) -> Unit,
    onClose: () -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
//        topBar = {
//            MediumTopAppBar(
//                title = { Text("Cat") },
//                actions = {
//                    IconButton(onClick = { onClose() }) {
//                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Close")
//                    }
//                }
//            )
//        },
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
                            IconButton(onClick = { onClose() }) {
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
                                Text(text = cat.name, style = MaterialTheme.typography.headlineMedium)
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
                                mainAxisSpacing = 8.dp, // Razmak između čipova u istom redu
                                crossAxisSpacing = 8.dp // Razmak između redova
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

                                eventPublisher(
                                    CatDetailsContract.CatDetailsEvent.OpenWiki(cat.wikipedia_url?.toUri(), context)

                                )
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
        }
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