package com.yanncha.timeslotpro.ui.coach.students

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentProfileBinding
import com.yanncha.timeslotpro.databinding.FragmentStudentsBinding
import com.yanncha.timeslotpro.ui.user.profile.ProfileViewModel
import com.yanncha.timeslotpro.util.picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentsFragment : Fragment() {

    private val viewModel: StudentsViewModel by viewModels()
    private var _binding: FragmentStudentsBinding? = null
    private val binding: FragmentStudentsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudentsBinding.inflate(inflater, container, false)

        val studentsAdapter = StudentsListAdapter(viewModel)

        with(binding){

            viewModel.coachInfosLiveData.observe(viewLifecycleOwner){coach ->
                picasso(coach.url_image, ivStudentsCoachAvatar)
                tvStudentsCoachUsername.text = getString(R.string.coach_name, coach.prenom, coach.nom)
            }

            rvStudents.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = studentsAdapter
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refresh()
                swipeRefreshLayout.isRefreshing = false
            }
        }

        viewModel.usersListLiveData.observe(viewLifecycleOwner){students->
            studentsAdapter.submitList(students)
        }

        viewModel.userMessageLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}