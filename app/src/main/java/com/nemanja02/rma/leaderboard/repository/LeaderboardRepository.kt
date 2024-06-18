package com.nemanja02.rma.leaderboard.repository

import com.nemanja02.rma.cats.api.CatsApi
import com.nemanja02.rma.db.AppDatabase
import com.nemanja02.rma.leaderboard.api.LeaderboardApi
import com.nemanja02.rma.leaderboard.api.model.LeaderboardApiModel
import com.nemanja02.rma.leaderboard.api.model.LeaderboardPostApiModel
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
) {
    suspend fun fetchLeaderboard(category: String) = leaderboardApi.getLeaderboard(category)

    suspend fun postLeaderboard(leaderboardPostApiModel: LeaderboardPostApiModel) = leaderboardApi.postLeaderboard(leaderboardPostApiModel)
}