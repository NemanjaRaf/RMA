package com.nemanja02.rma.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemanja02.rma.leaderboard.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel(){


    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()
    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) = _state.update(reducer)

    init {
        fetchRankings()
    }

    private fun fetchRankings() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    val rankings = leaderboardRepository.fetchLeaderboard("1")
                    setState {
                        copy(rankings = rankings, loading = false)
                    }
                }
            } catch (error: Exception) {
                Log.e("Greska", "Message", error)
            } finally {
                setState { copy(loading = false) }
            }
        }
    }


}