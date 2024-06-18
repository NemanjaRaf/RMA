package com.nemanja02.rma.leaderboard.api.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardPostApiModel (
    val category: Int,
    val nickname: String,
    val result: Float,
)