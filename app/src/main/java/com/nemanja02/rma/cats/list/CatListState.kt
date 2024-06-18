package com.nemanja02.rma.cats.list

import com.nemanja02.rma.cats.model.CatUiModel

data class CatListState(
    val loading: Boolean = true,
    val updating: Boolean = false,
    val cats: List<CatUiModel> = emptyList(),
)