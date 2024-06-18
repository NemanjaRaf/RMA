package com.nemanja02.rma.leaderboard.api.model

import com.nemanja02.rma.cats.api.model.CatApiImage
import com.nemanja02.rma.cats.api.model.CatApiModel
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class LeaderboardApiModel (
    val category: Int,
    val nickname: String,
    val result: Float,
    val createdAt: Long,
)