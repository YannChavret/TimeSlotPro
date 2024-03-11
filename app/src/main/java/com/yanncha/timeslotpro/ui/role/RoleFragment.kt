package com.yanncha.timeslotpro.ui.role

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentLoginCoachBinding
import com.yanncha.timeslotpro.databinding.FragmentRoleBinding
import com.yanncha.timeslotpro.ui.coach.login.LoginCoachViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoleFragment : Fragment() {

    private val viewModel: RoleViewModel by viewModels()
    private var _binding: FragmentRoleBinding? = null
    private val binding: FragmentRoleBinding
        get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoleBinding.inflate(inflater, container, false)

        with(binding) {
            btnRoleCoach.setOnClickListener{
                findNavController().navigate(RoleFragmentDirections.actionRoleFragment2ToLoginCoachFragment())
            }
            btnRoleStudent.setOnClickListener{
                findNavController().navigate(RoleFragmentDirections.actionRoleFragment2ToLoginUserFragment())
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}