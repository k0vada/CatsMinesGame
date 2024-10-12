package com.example.catsminesgame.screens.endGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.catsminesgame.R
import com.example.catsminesgame.databinding.FragmentEndGameBinding


class EndGameFragment : Fragment() {

    private lateinit var binding: FragmentEndGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEndGameBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalClicks = arguments?.getInt("totalClicks") ?: 0
        val mouseHits = arguments?.getInt("mouseHits") ?: 0
        val duration = arguments?.getLong("duration") ?: 0L
        val accuracy = arguments?.getInt("accuracy") ?: 0

        binding.duration.text = formatDuration(duration)
        binding.mouseHits.text = mouseHits.toString()
        binding.totalClicks.text = totalClicks.toString()
        binding.accuracy.text = "$accuracy%"

        binding.exitButton.setOnClickListener{
            findNavController().navigate(R.id.action_endGameFragment_to_startFragment)
        }

        binding.replayButton.setOnClickListener{
            findNavController().navigate(R.id.action_endGameFragment_to_gameFragment)
        }
    }

    private fun formatDuration(gameTimeMillis: Long): String {
        val seconds = (gameTimeMillis / 1000) % 60
        val minutes = (gameTimeMillis / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}