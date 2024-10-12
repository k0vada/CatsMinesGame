package com.example.catsminesgame.db.repository

import androidx.lifecycle.LiveData
import com.example.catsminesgame.db.dao.GameResultDao
import com.example.catsminesgame.model.GameResult

class GameRepositoryRealisation(private val gameResultDao: GameResultDao): GameRepository {
    override suspend fun insertGameResult(gameResult: GameResult) {
        gameResultDao.insertGameResult(gameResult)
    }

    override fun getLast10GameResults(): LiveData<List<GameResult>> {
        return gameResultDao.getLast10GameResults()
    }

    override fun getTotalMouseHits(): LiveData<Int> {
        return gameResultDao.getTotalMouseHits()
    }
}