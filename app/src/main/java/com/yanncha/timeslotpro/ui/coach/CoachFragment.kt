package com.yanncha.timeslotpro.ui.coach

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentCoachBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoachFragment : Fragment() {

    private val viewModel: CoachViewModel by viewModels()
    private var _binding: FragmentCoachBinding? = null
    private val binding: FragmentCoachBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoachBinding.inflate(inflater, container, false)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavBar: BottomNavigationView =
            binding.root.findViewById(R.id.bottomNavigationView2)
        bottomNavBar.setupWithNavController(navController)

        with(binding) {
            btnLogout.setOnClickListener {
                viewModel.logOut()
                findNavController().navigate(CoachFragmentDirections.actionCoachFragmentToSplashFragment())
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}