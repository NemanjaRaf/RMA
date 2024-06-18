package com.nemanja02.rma.cats.mappers

import com.nemanja02.rma.cats.api.model.CatApiModel
import com.nemanja02.rma.cats.db.CatData
import com.nemanja02.rma.cats.model.CatUiModel

fun CatApiModel.asCatDbModel(): CatData {
    return CatData(
        id = id,
        name = name,
        description = description,
        alt_names = alt_names,
        temperament = temperament,
        image = image,
        weight = weight,
        energy_level = energy_level,
        affection_level = affection_level,
        child_friendly = child_friendly,
        dog_friendly = dog_friendly,
        stranger_friendly = stranger_friendly,
        life_span = life_span,
        origin = origin,
        country_codes = country_codes,
        wikipedia_url = wikipedia_url,
        rare = rare,
    )
}
