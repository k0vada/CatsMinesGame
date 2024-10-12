package com.example.catsminesgame.screens.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.catsminesgame.databinding.FragmentGamePauseDialogBinding


class GamePauseDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentGamePauseDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGamePauseDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueButton.setOnClickListener{
            parentFragmentManager.setFragmentResult("game_pause_result", Bundle().apply {
                putString("action", "resume")
            })
            dismiss()
        }

        binding.exitButton.setOnClickListener{
            parentFragmentManager.setFragmentResult("game_pause_result", Bundle().apply {
                putString("action", "exit")
            })
            dismiss()
        }
    }
}