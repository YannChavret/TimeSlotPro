package com.yanncha.timeslotpro.ui.coach.register

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
import com.yanncha.timeslotpro.util.setFocusChangeBlue
import com.yanncha.timeslotpro.util.setFocusChangePink
import com.yanncha.timeslotpro.util.setFocusChangeYellow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterCoachFragment : Fragment() {

    private val viewModel: RegisterCoachViewModel by viewModels()
    private var _binding: FragmentRegisterCoachBinding? = null
    private val binding : FragmentRegisterCoachBinding
        get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterCoachBinding.inflate(inflater, container, false)

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
                findNavController().navigate(RegisterCoachFragmentDirections.actionRegisterCoachFragmentToCoachFragment())
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