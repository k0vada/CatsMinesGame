package com.example.catsminesgame.screens.start

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.catsminesgame.REPOSITORY
import com.example.catsminesgame.db.GameDatabase
import com.example.catsminesgame.db.repository.GameRepositoryRealisation

class StartViewModel(application: Application) : AndroidViewModel(application){

    private val context = application

    fun initDatabase(){
        val daoBook = GameDatabase.getDatabase(context).gameResultDao()
        REPOSITORY = GameRepositoryRealisation(daoBook)
    }

    fun getTotalMouseHits(): LiveData<Int> {
        return REPOSITORY.getTotalMouseHits()
    }
}