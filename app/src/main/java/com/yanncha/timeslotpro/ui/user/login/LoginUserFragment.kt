package com.yanncha.timeslotpro.ui.user.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentLoginUserBinding
import com.yanncha.timeslotpro.util.setFocusChangePink
import com.yanncha.timeslotpro.util.setFocusChangeYellow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginUserFragment : Fragment() {

    private val viewModel: LoginUserViewModel by viewModels()
    private var _binding: FragmentLoginUserBinding? = null
    private val binding: FragmentLoginUserBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginUserBinding.inflate(inflater, container, false)

        with(binding) {
            etIdentifiantUser.setFocusChangeYellow()
            etMdpUser.setFocusChangePink()
            btnLoginUser.setOnClickListener() {
                viewModel.checkFieldsAndLogIn(
                    etIdentifiantUser.text.toString(),
                    etMdpUser.text.toString()
                )
            }
            tvLinkRegister.setOnClickListener() {
                findNavController().navigate(LoginUserFragmentDirections.actionLoginUserFragmentToRegisterUserFragment())
            }
        }

        viewModel.userMessageLiveData.observe(viewLifecycleOwner) {
            if (it == R.string.welcome_user) {
                findNavController().navigate(LoginUserFragmentDirections.actionLoginUserFragmentToUserFragment())
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