package com.nemanja02.rma.cats.list

import com.nemanja02.rma.cats.list.model.Breed

interface CatListContract {
    data class CatListState(
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val cats: List<Breed> = emptyList(),
        val filteredCats: List<Breed> = emptyList(),
    )

    sealed class CatListEvent {
        data class SearchQueryChanged(val query: String) : CatListEvent()
        data object ClearSearch : CatListEvent()
        data object CloseSearchMode : CatListEvent()
        data object OpenSearchMode : CatListEvent()
        data object Dummy : CatListEvent()
    }
}