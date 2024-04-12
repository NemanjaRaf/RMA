package com.nemanja02.rma.cats.details

import android.content.Context
import android.net.Uri
import com.nemanja02.rma.cats.list.model.Breed

interface CatDetailsContract {
    data class CatDetailsState(
        val loading: Boolean = false,
        val cat: Breed? = null,
    )

    sealed class CatDetailsEvent {
        data class OpenWiki(val url: Uri?, val context: Context) : CatDetailsEvent()
        data object Dummy : CatDetailsEvent()
    }
}