package com.example.catsminesgame.screens.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.catsminesgame.REPOSITORY
import com.example.catsminesgame.db.repository.GameRepository
import com.example.catsminesgame.model.GameResult

class StatisticsViewModel : ViewModel() {
    fun getLast10GameResults() : LiveData<List<GameResult>> {
        return REPOSITORY.getLast10GameResults()
    }
}