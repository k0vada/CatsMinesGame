package com.example.catsminesgame.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "game_results")
data class GameResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalClicks: Int,
    val mouseHits: Int,
    val duration: Long,
    val endTime: Long
    ) : Serializable
