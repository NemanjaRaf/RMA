package com.nemanja02.rma.cats.api.model

import com.nemanja02.rma.cats.list.model.Image
import kotlinx.serialization.Serializable

@Serializable
data class CatApiModel(
    val id: String,
    val name: String,
    val description: String,
    val alt_names: String,
    val temperament: String,
    val image: CatApiImage,
    val weight: CatApiWeight,
    var energy_level: Int,
    var affection_level: Int,
    var child_friendly: Int,
    var dog_friendly: Int,
    var stranger_friendly: Int,
    var life_span: String,
    var origin : String,
    var country_codes: String,
    var wikipedia_url: String?,
    var rare : Int,
)

@Serializable
data class CatApiImage(
    val id: String,
    val url: String,
) {
    fun asImageUIModel(): Image {
        return Image(id, url)
    }
}

@Serializable
data class CatApiWeight(
    val imperial: String,
    val metric: String,
) {
    fun asWeightUIModel(): com.nemanja02.rma.cats.list.model.Weight {
        return com.nemanja02.rma.cats.list.model.Weight(imperial, metric)
    }
}