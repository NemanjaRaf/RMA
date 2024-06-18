package com.nemanja02.rma.leaderboard

import com.nemanja02.rma.leaderboard.api.model.LeaderboardApiModel


data class LeaderboardState (
    val loading: Boolean = true,
    val updating: Boolean = false,
    val rankings: List<LeaderboardApiModel> = emptyList(),
)