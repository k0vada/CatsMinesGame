package com.example.catsminesgame.screens.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.catsminesgame.R
import com.example.catsminesgame.databinding.FragmentStartBinding
import com.google.android.material.navigation.NavigationView
import androidx.lifecycle.Observer


class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(layoutInflater, container, false)
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        viewModel.initDatabase()

        binding.playButton.setOnClickListener {
            openGameSettingsDialog()
        }

        viewModel.getTotalMouseHits().observe(viewLifecycleOwner, Observer { totalMouseHits ->
            binding.mouseCount.text = totalMouseHits.toString()
        })

        binding.menuButton.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawer(navigationView)
            when (menuItem.itemId) {
                R.id.nav_statistics -> {
                    findNavController().navigate(R.id.action_startFragment_to_statisticsFragment)
                    true
                }
                R.id.nav_settings -> {
                    findNavController().navigate(R.id.action_startFragment_to_settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun openGameSettingsDialog() {
        val dialog = GameSettingsDialogFragment()
        dialog.show(parentFragmentManager, "gameSettings")
    }
}