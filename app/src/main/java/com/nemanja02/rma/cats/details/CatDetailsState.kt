package com.nemanja02.rma.cats.details

import com.nemanja02.rma.cats.model.CatUiModel

data class CatDetailsState(
    val loading: Boolean = true,
    val updating: Boolean = false,
    val cat: CatUiModel? = null,
)