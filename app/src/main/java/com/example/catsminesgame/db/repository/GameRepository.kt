package com.example.catsminesgame.db.repository

import androidx.lifecycle.LiveData
import com.example.catsminesgame.model.GameResult

interface GameRepository {
    suspend fun insertGameResult(gameResult: GameResult)
    fun getLast10GameResults(): LiveData<List<GameResult>>
    fun getTotalMouseHits(): LiveData<Int>
}