package com.example.catsminesgame.screens.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catsminesgame.R
import com.example.catsminesgame.adapter.StatisticsAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.catsminesgame.databinding.FragmentEndGameBinding
import com.example.catsminesgame.databinding.FragmentStatisticsBinding


class StatisticsFragment : Fragment() {

    private lateinit var statisticsViewModel: StatisticsViewModel
    private lateinit var statisticsAdapter: StatisticsAdapter
    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding.gamesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        statisticsAdapter = StatisticsAdapter(emptyList())
        recyclerView.adapter = statisticsAdapter
        statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)

        statisticsViewModel.getLast10GameResults().observe(viewLifecycleOwner, Observer { gameResults ->
            statisticsAdapter.setGameResults(gameResults)
        })

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_statisticsFragment_to_startFragment)
        }
    }
}