package com.nemanja02.rma.cats.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nemanja02.rma.cats.api.model.CatApiImage
import com.nemanja02.rma.cats.api.model.CatApiWeight

@Entity
data class CatData(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val alt_names: String? = null,
    val temperament: String,
    @Embedded(prefix = "image_") val image: CatApiImage? = null,
    @Embedded val weight: CatApiWeight,
    var energy_level: Int,
    var affection_level: Int,
    var child_friendly: Int,
    var dog_friendly: Int,
    var stranger_friendly: Int,
    var life_span: String,
    var origin: String,
    var country_codes: String,
    var wikipedia_url: String?,
    var rare: Int,
    )
