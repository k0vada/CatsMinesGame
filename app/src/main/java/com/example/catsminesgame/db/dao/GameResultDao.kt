package com.example.catsminesgame.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.catsminesgame.model.GameResult

@Dao
interface GameResultDao {
    @Insert
    suspend fun insertGameResult(gameResult: GameResult)

    @Query("SELECT * FROM game_results ORDER BY endTime DESC LIMIT 10")
    fun getLast10GameResults(): LiveData<List<GameResult>>

    @Query("SELECT SUM(mouseHits) FROM game_results")
    fun getTotalMouseHits(): LiveData<Int>
}