package com.nemanja02.rma.cats.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemanja02.rma.cats.api.model.CatApiImage
import com.nemanja02.rma.cats.api.model.CatApiWeight
import com.nemanja02.rma.cats.db.CatData
import com.nemanja02.rma.cats.model.CatUiModel
import com.nemanja02.rma.cats.model.Image
import com.nemanja02.rma.cats.model.Weight
import com.nemanja02.rma.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CatListViewModel @Inject constructor(
    private val catRepository: CatsRepository,
) : ViewModel(){


    private val _state = MutableStateFlow(CatListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatListState.() -> CatListState) = _state.update(reducer)

    init {
        fetchAllCats()
        observeCats()
    }

    private fun observeCats() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            catRepository.observeAllCats()
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(
                            loading = false,
                            cats = it.map { it.asCatUiModel() },
                        )
                    }
                }
        }
    }

    private fun fetchAllCats() {
        viewModelScope.launch {
            setState { copy(updating = true) }
            try {
                withContext(Dispatchers.IO) {
                    catRepository.fetchAll()
                }
            } catch (error: Exception) {
                // TODO Handle error
                Log.e("Greska", "Message", error)
            } finally {
                setState { copy(updating = false) }
            }
        }
    }

    private fun CatData.asCatUiModel() = CatUiModel(
        id = id,
        name = name,
        description = description,
        image = image?.asImage(),
        weight = weight?.asWeight(),
        energy_level = energy_level,
        affection_level = affection_level,
        child_friendly = child_friendly,
        dog_friendly = dog_friendly,
        stranger_friendly = stranger_friendly,
        life_span = life_span,
        origin = origin,
        country_codes = country_codes,
        wikipedia_url = wikipedia_url,
        alt_names = alt_names,
        temperament = temperament,
        rare = rare
    )

    private fun CatApiImage.asImage() = Image(
        url = url,
    )

    private fun CatApiWeight.asWeight() = Weight(
        imperial = imperial,
        metric = metric,
    )


}