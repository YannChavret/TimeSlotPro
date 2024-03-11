package com.yanncha.timeslotpro.ui.user.book

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentBookCourseBinding
import com.yanncha.timeslotpro.databinding.FragmentMainCoachBinding
import com.yanncha.timeslotpro.ui.coach.main.CoursesListAdapter
import com.yanncha.timeslotpro.ui.coach.main.MainCoachFragmentDirections
import com.yanncha.timeslotpro.ui.coach.main.MainCoachViewModel
import com.yanncha.timeslotpro.ui.user.main.MainUserFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookCourseFragment : Fragment() {

    private val viewModel: BookCourseViewModel by viewModels()
    private var _binding: FragmentBookCourseBinding? = null
    private val binding: FragmentBookCourseBinding
        get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookCourseBinding.inflate(inflater, container, false)

        val coursesAdapter = CourseListWithCoachAdapter(viewModel)

        with(binding) {

            rvBookUser.apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = coursesAdapter
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchAllCourses()
                swipeRefreshLayout.isRefreshing = false
            }
        }

        viewModel.selectedCourseLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {course->
                findNavController().navigate(BookCourseFragmentDirections.actionBookCourseFragmentToBookDetailsCourseFragment(course.courseId))
            }
        }

        viewModel.coursesLiveData.observe(viewLifecycleOwner) { cours ->
            coursesAdapter.submitList(cours)
        }

        viewModel.userMessageLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        viewModel.fetchAllCourses()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
}