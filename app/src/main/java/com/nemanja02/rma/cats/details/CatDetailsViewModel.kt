package com.nemanja02.rma.cats.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemanja02.rma.auth.AuthStore
import com.nemanja02.rma.cats.api.model.CatApiImage
import com.nemanja02.rma.cats.api.model.CatApiWeight
import com.nemanja02.rma.cats.db.CatData
import com.nemanja02.rma.cats.list.CatListState
import com.nemanja02.rma.cats.model.CatUiModel
import com.nemanja02.rma.cats.model.Image
import com.nemanja02.rma.cats.model.Weight
import com.nemanja02.rma.cats.repository.CatsRepository
import com.nemanja02.rma.navigation.catID
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
class CatDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catRepository: CatsRepository,
    private val authStore: AuthStore
) : ViewModel(){

    private val catId = savedStateHandle.catID;

    private val _state = MutableStateFlow(CatDetailsState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatDetailsState.() -> CatDetailsState) = _state.update(reducer)

    init {
        observeCat()
    }

    private fun observeCat() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            catRepository.fetchCatById(catId)
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(
                            loading = false,
                            cat = it.asCatUiModel(),
                        )
                    }
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