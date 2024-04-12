package com.nemanja02.rma.cats.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.nemanja02.rma.cats.list.model.Breed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun NavGraphBuilder.cats(
    route: String,
    onCatSelected: (String) -> Unit,
) = composable(route) {
    var catListViewModel = viewModel<CatListViewModel>()

    val state = catListViewModel.state.collectAsState()
    CatListScreen(
        state = state.value,
        eventPublisher = {
            catListViewModel.setEvent(it)
        },
        onCatSelected = onCatSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListScreen(
    state: CatListContract.CatListState,
    eventPublisher: (CatListContract.CatListEvent) -> Unit,
    onCatSelected: (String) -> Unit,
) {
    Scaffold(
        content = { paddingValues ->


            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (state.isSearchMode) {
                        OutlinedTextField(
                            value = state.query,
                            onValueChange = { query ->
                                eventPublisher(
                                    CatListContract.CatListEvent.SearchQueryChanged(
                                        query
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            placeholder = { Text("Search cats...") },
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    eventPublisher(CatListContract.CatListEvent.ClearSearch)
                                    scrollToTop(listState = LazyListState())
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear Search"
                                    )
                                }
                            },

                        )
                    } else {
                        MediumTopAppBar(
                            title = { Text("Cats") },
                            actions = {
                                IconButton(onClick = { eventPublisher(CatListContract.CatListEvent.OpenSearchMode) }) {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                                }
                            }
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),

                    ) {
                        items(
                            items = if (state.query.isNotEmpty()) state.filteredCats else state.cats,
                            key = { it.id },
                        ) { cat ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .padding(bottom = 16.dp)
                                    .clickable { onCatSelected(cat.id) },
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
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(
                                        text = if (cat.description.length > 250) "${
                                            cat.description.take(
                                                250
                                            )
                                        }..." else cat.description,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        cat.temperament.split(",").shuffled().take(3)
                                            .forEach { temperament ->
                                                AssistChip(
                                                    onClick = { /* Handle chip click */ },
                                                    label = { Text(temperament.trim()) }
                                                )
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

class UserListStateParameterProvider : PreviewParameterProvider<CatListContract.CatListState> {
    override val values: Sequence<CatListContract.CatListState> = sequenceOf(
        CatListContract.CatListState(
            loading = true,
        ),
        CatListContract.CatListState(
            loading = false,
        ),

    )
}

fun scrollToTop(listState: LazyListState) {
    CoroutineScope(Dispatchers.Main).launch {
        listState.animateScrollToItem(index = 0)
    }
}

@Preview
@Composable
private fun PreviewCatList(
    @PreviewParameter(UserListStateParameterProvider::class) userListState: CatListContract.CatListState,
) {
    CatListScreen(
        state = userListState,
        eventPublisher = {},
        onCatSelected = {},
    )
}
