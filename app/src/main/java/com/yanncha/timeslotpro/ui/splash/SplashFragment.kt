package com.yanncha.timeslotpro.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()
    private var _binding: FragmentSplashBinding? = null
    private val binding : FragmentSplashBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.myPrefsLiveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                delay(3000)
                if(it.getString("role","").isNullOrEmpty()) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRoleFragment2())
                }else if (it.getString("role","") == "user"){
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToUserFragment())
                }else {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToCoachFragment())
                }
                requireActivity().window.decorView.systemUiVisibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}