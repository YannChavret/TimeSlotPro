package com.yanncha.timeslotpro.ui.user.register

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
import com.yanncha.timeslotpro.databinding.FragmentRegisterCoachBinding
import com.yanncha.timeslotpro.databinding.FragmentRegisterUserBinding
import com.yanncha.timeslotpro.ui.coach.register.RegisterCoachViewModel
import com.yanncha.timeslotpro.util.setFocusChangeBlue
import com.yanncha.timeslotpro.util.setFocusChangePink
import com.yanncha.timeslotpro.util.setFocusChangeYellow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterUserFragment : Fragment() {

    private val viewModel: RegisterUserViewModel by viewModels()
    private var _binding: FragmentRegisterUserBinding? = null
    private val binding : FragmentRegisterUserBinding
        get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterUserBinding.inflate(inflater, container, false)

        with(binding){
            etNom.setFocusChangeBlue()
            etPrenom.setFocusChangeYellow()
            etIdentifiant.setFocusChangePink()
            etMdp.setFocusChangeBlue()
            etConfirm.setFocusChangeYellow()
            etUrl.setFocusChangePink()

            btnRegister.setOnClickListener{
                viewModel.checkFieldsAndSignUp(
                    etNom.text.toString(),
                    etPrenom.text.toString(),
                    etIdentifiant.text.toString(),
                    etMdp.text.toString(),
                    etConfirm.text.toString(),
                    etUrl.text.toString(),
                )
            }
        }

        viewModel.userMessageLiveData.observe(viewLifecycleOwner) {
            if(it==R.string.registration_successful){
                findNavController().navigate(RegisterUserFragmentDirections.actionRegisterUserFragmentToUserFragment())
            } else {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }


}