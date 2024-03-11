package com.yanncha.timeslotpro.ui.coach.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentLoginCoachBinding
import com.yanncha.timeslotpro.util.setFocusChangePink
import com.yanncha.timeslotpro.util.setFocusChangeYellow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginCoachFragment : Fragment() {

    private val viewModel: LoginCoachViewModel by viewModels()
    private var _binding: FragmentLoginCoachBinding? = null
    private val binding: FragmentLoginCoachBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginCoachBinding.inflate(inflater, container, false)

        with(binding) {
            etIdentifiant.setFocusChangeYellow()
            etMdp.setFocusChangePink()
            btnLogin.setOnClickListener() {
                viewModel.checkFieldsAndLogIn(
                    etIdentifiant.text.toString(),
                    etMdp.text.toString()
                )
            }
            tvLinkRegister.setOnClickListener() {
                findNavController().navigate(LoginCoachFragmentDirections.actionLoginCoachFragmentToRegisterCoachFragment())
            }
        }

        viewModel.userMessageLiveData.observe(viewLifecycleOwner) {
            if (it == R.string.welcome_user) {
                findNavController().navigate(LoginCoachFragmentDirections.actionLoginCoachFragmentToCoachFragment())
            } else {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}