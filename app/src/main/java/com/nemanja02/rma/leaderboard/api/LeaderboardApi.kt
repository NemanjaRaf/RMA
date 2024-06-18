package com.nemanja02.rma.leaderboard.api

import com.nemanja02.rma.leaderboard.api.model.LeaderboardApiModel
import com.nemanja02.rma.leaderboard.api.model.LeaderboardPostApiModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApi {

    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") category: String
    ): List<LeaderboardApiModel>

    @POST("leaderboard")
    suspend fun postLeaderboard(@Body leaderboardPostApiModel: LeaderboardPostApiModel)
}