package com.nemanja02.rma.cats.details

import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemanja02.rma.cats.api.model.CatApiModel
import com.nemanja02.rma.cats.list.CatListContract
import com.nemanja02.rma.cats.list.model.Breed
import com.nemanja02.rma.cats.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatDetailsViewModel(
    private val catId: String,
    private val repository: CatsRepository = CatsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CatDetailsContract.CatDetailsState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatDetailsContract.CatDetailsState.() -> CatDetailsContract.CatDetailsState) = _state.update(reducer)

    private val events = MutableSharedFlow<CatDetailsContract.CatDetailsEvent>()
    fun setEvent(event: CatDetailsContract.CatDetailsEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchCatDetails()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    CatDetailsContract.CatDetailsEvent.Dummy -> TODO()
                    is CatDetailsContract.CatDetailsEvent.OpenWiki -> {
                        val customTabsIntent = CustomTabsIntent.Builder().build()
                        it.url?.let { it1 -> customTabsIntent.launchUrl(it.context, it1) }
                    }
                }
            }
        }
    }

    private fun fetchCatDetails() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val cat = withContext(Dispatchers.IO) {
                    repository.getBreedById(catId).asCatUIModel()
                }
                val images = withContext(Dispatchers.IO) {
                    repository.getBreedImages(catId)
                }

                cat.image = images?.asImageUIModel()
                println("----------------")
                println(cat)
                setState { copy(cat = cat) }
            } catch (error: Exception) {
                // TODO Handle error
                println(error)
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