package com.example.catsminesgame.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catsminesgame.R
import com.example.catsminesgame.model.GameResult
import java.util.concurrent.TimeUnit


class StatisticsAdapter(private var gameResults: List<GameResult>) : RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {
    class StatisticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeAgoTextView: TextView = itemView.findViewById(R.id.game_time_ago)
        val durationTextView: TextView = itemView.findViewById(R.id.game_duration)
        val mouseHitsTextView: TextView = itemView.findViewById(R.id.game_mouse_hits)
        val totalClicksTextView: TextView =  itemView.findViewById(R.id.game_total_clicks)
        val accuracyTextView: TextView = itemView.findViewById(R.id.game_accuracy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return StatisticsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return gameResults.size
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        val gameResult = gameResults[position]
        holder.timeAgoTextView.text = calculateTimeAgo(gameResult.endTime)
        holder.durationTextView.text = calculateDuration(gameResult.duration)
        holder.mouseHitsTextView.text = "Пoймано мышек: ${gameResult.mouseHits}"
        holder.totalClicksTextView.text = "Количество попыток: ${gameResult.totalClicks}"
        holder.accuracyTextView.text = "Меткость: ${if (gameResult.totalClicks > 0) (gameResult.mouseHits.toDouble() / gameResult.totalClicks * 100).toInt() else 0}%"
    }

    fun setGameResults(gameResults: List<GameResult>) {
        this.gameResults = gameResults
        notifyDataSetChanged()
    }

    private fun calculateTimeAgo(endTime: Long): String {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - endTime

        val minutesAgo = TimeUnit.MILLISECONDS.toMinutes(timeDifference)
        val hoursAgo = TimeUnit.MILLISECONDS.toHours(timeDifference)
        val daysAgo = TimeUnit.MILLISECONDS.toDays(timeDifference)

        return when {
            minutesAgo < 60 -> "Игра $minutesAgo мин. назад"
            hoursAgo < 24 -> "Игра $hoursAgo ч. назад"
            else -> "Игра $daysAgo д. назад"
        }
    }

    private fun calculateDuration(duration: Long): String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / 1000) / 60
        val result = String.format("%02d:%02d", minutes, seconds)
        return "Время игры: $result"
    }
}