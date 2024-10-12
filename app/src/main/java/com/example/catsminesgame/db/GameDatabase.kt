package com.example.catsminesgame.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.catsminesgame.db.dao.GameResultDao
import com.example.catsminesgame.model.GameResult

@Database(entities = [GameResult::class], version = 1)
abstract class GameDatabase: RoomDatabase() {

    abstract fun gameResultDao(): GameResultDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}