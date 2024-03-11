package com.yanncha.timeslotpro.ui.coach.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentBookDetailsCourseBinding
import com.yanncha.timeslotpro.databinding.FragmentDetailsCourseBinding
import com.yanncha.timeslotpro.ui.user.book_details.BookDetailsCourseFragmentArgs
import com.yanncha.timeslotpro.ui.user.book_details.BookDetailsCourseViewModel
import com.yanncha.timeslotpro.ui.user.book_details.UserListAdapter
import com.yanncha.timeslotpro.util.picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsCourseFragment : Fragment() {

    private val args: DetailsCourseFragmentArgs by navArgs()

    private val viewModel: DetailsCourseViewModel by viewModels()
    private var _binding: FragmentDetailsCourseBinding? = null
    private val binding: FragmentDetailsCourseBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsCourseBinding.inflate(inflater, container, false)

        val userAdapter = UserListAdapter(viewModel)

        viewModel.fetchCourseById(args)
        viewModel.fetchUsersByCourse(args)


        with(binding) {
            rvDetails.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = userAdapter
            }
            viewModel.courseDetailLiveData.observe(viewLifecycleOwner) { course ->
                binding.course = course
            }

            viewModel.operationCompleted.observe(viewLifecycleOwner) { completed ->
                if (completed) {
                    findNavController().popBackStack()
                    viewModel.resetOperationCompleted()
                }
            }
        }

        viewModel.usersListLiveData.observe(viewLifecycleOwner) { users ->
            userAdapter.submitList(users)
        }

        viewModel.userMessageLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}