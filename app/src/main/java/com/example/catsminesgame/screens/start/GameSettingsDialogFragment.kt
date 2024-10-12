package com.example.catsminesgame.screens.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.catsminesgame.R
import com.example.catsminesgame.databinding.FragmentGameSettingsDialogBinding


class GameSettingsDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentGameSettingsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameSettingsDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.quantitySeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.quantityValue.text = "$progress/5"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.speedSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.speedValue.text = "$progress/3"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.playButton.setOnClickListener {
            val speed = binding.speedSeekbar.progress
            val quantity = binding.quantitySeekbar.progress
            val bundle = Bundle()
            bundle.putInt("speed", speed)
            bundle.putInt("quantity", quantity)
            findNavController().navigate(R.id.action_startFragment_to_gameFragment, bundle)
            dismiss()
        }
    }
}