package com.yanncha.timeslotpro.ui.user.book_details

import android.annotation.SuppressLint
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
import com.yanncha.timeslotpro.ui.user.book.CourseListWithCoachAdapter
import com.yanncha.timeslotpro.util.picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailsCourseFragment : Fragment() {

    private val args: BookDetailsCourseFragmentArgs by navArgs()

    private val viewModel: BookDetailsCourseViewModel by viewModels()
    private var _binding: FragmentBookDetailsCourseBinding? = null
    private val binding: FragmentBookDetailsCourseBinding
        get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookDetailsCourseBinding.inflate(inflater, container, false)

        val userAdapter = UserListAdapter(viewModel)

        viewModel.fetchCourseById(args)
        viewModel.fetchUsersByCourse(args)


        with(binding) {
            rvBookDetailsUserList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = userAdapter
            }
            viewModel.courseDetailLiveData.observe(viewLifecycleOwner) { course ->
                binding.course = course

                picasso(course.urlImageCoach, ivAvatarCoach)

            }

            viewModel.operationCompleted.observe(viewLifecycleOwner) { completed ->
                if (completed) {
                    findNavController().popBackStack()
                    viewModel.resetOperationCompleted()
                }
            }

            viewModel.isExistingReservationLiveData.observe(viewLifecycleOwner) { isExistingRes ->
                if (isExistingRes) {
                    binding.btnReserver.text = "Annuler ma réservation"
                    binding.btnReserver.setOnClickListener {
                        viewModel.cancelReservation(course?.courseId!!)
                    }
                } else {
                    binding.btnReserver.text = "Réserver"
                    binding.btnReserver.setOnClickListener {
                        viewModel.bookCourse()
                    }
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