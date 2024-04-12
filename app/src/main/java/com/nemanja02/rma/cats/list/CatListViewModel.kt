package com.nemanja02.rma.cats.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemanja02.rma.cats.api.model.CatApiModel
import com.nemanja02.rma.cats.list.model.Breed
import com.nemanja02.rma.cats.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class CatListViewModel(
    private val repository: CatsRepository = CatsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CatListContract.CatListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatListContract.CatListState.() -> CatListContract.CatListState) = _state.update(reducer)

    private val events = MutableSharedFlow<CatListContract.CatListEvent>()
    fun setEvent(event: CatListContract.CatListEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchAllCats()
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            events
                .filterIsInstance<CatListContract.CatListEvent.SearchQueryChanged>()
                .debounce(2.seconds)
                .collect {
                    // Called only when search query was changed
                    // and once 2 seconds have passed after the last char


                    // This is helpful to avoid trigger expensive calls
                    // on every character change
                    //s
                    //si
                    //sib
                    //sibi
                    //sibir
                    //sibirs
                }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is CatListContract.CatListEvent.SearchQueryChanged -> {
                        println(it)
                        setState { copy(query = it.query, filteredCats = state.value.cats.filter { cat ->
                            cat.name.contains(it.query, ignoreCase = true) ||
                                    cat.description.contains(it.query, ignoreCase = true)
                        })}
                    }
                    is CatListContract.CatListEvent.OpenSearchMode -> setState { copy(isSearchMode = true) }
                    is CatListContract.CatListEvent.ClearSearch -> setState { copy(query = "", isSearchMode = false) }
                    is CatListContract.CatListEvent.CloseSearchMode -> setState { copy(isSearchMode = false) }

//                    CatListContract.CatListEvent.Dummy -> TODO()
                    else -> {}
                }
            }
        }
    }

    private fun fetchAllCats() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val users = withContext(Dispatchers.IO) {
                    repository.getBreeds().map {
                        it.asCatUIModel()
                    }
                }

                setState { copy(cats = users) }
            } catch (error: Exception) {
                println("Error fetching users: $error")
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private fun CatApiModel.asCatUIModel() = Breed(
        id = id,
        name = name,
        description = description,
        origin = origin,
        temperament = temperament,
        image = image?.asImageUIModel(),
        weight = weight?.asWeightUIModel(),
        energy_level = energy_level,
        affection_level = affection_level,
        child_friendly = child_friendly,
        dog_friendly = dog_friendly,
        stranger_friendly = stranger_friendly,

        alt_names = alt_names,
        country_codes = country_codes,
        life_span = life_span,
        wikipedia_url = wikipedia_url,
        rare = rare,
    )
}