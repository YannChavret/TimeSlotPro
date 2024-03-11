package com.yanncha.timeslotpro.ui.coach.main

import android.os.Build
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
import com.yanncha.timeslotpro.databinding.FragmentMainCoachBinding
import com.yanncha.timeslotpro.ui.user.main.MainUserFragmentDirections
import com.yanncha.timeslotpro.util.ReservationFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainCoachFragment : Fragment() {

    private val viewModel: MainCoachViewModel by viewModels()
    private var _binding: FragmentMainCoachBinding? = null
    private val binding: FragmentMainCoachBinding
        get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainCoachBinding.inflate(inflater, container, false)

        val coursesAdapter = CoursesListAdapter(viewModel)
        var filter:ReservationFilter = ReservationFilter.ALL

        with(binding) {

            rvMainCoach.apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = coursesAdapter
            }

           swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchCoursesbyCoach()
                swipeRefreshLayout.isRefreshing = false
               rvMainCoach.layoutManager?.scrollToPosition(0)
            }

            segmented{
                initialCheckedIndex = 0
                initWithItems {
                    listOf("Tous", "Prévus", "Réalisés")
                }
                onSegmentChecked { segment ->
                    filter = when (segment.text.toString()) {
                        "Tous" -> ReservationFilter.ALL
                        "Prévus" -> ReservationFilter.FUTURE
                        "Réalisés" -> ReservationFilter.PAST
                        else -> ReservationFilter.ALL
                    }
                    viewModel.filterCourses(filter)
                }
            }
        }

        viewModel.coursesLiveData.observe(viewLifecycleOwner) { cours ->
            coursesAdapter.submitList(cours)
            binding.rvMainCoach.layoutManager?.scrollToPosition(0)
        }

        viewModel.selectedCourseLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { course ->
                findNavController().navigate(MainCoachFragmentDirections.actionMainCoachFragmentToDetailsCourseFragment(course.courseId))
            }
        }

        viewModel.userMessageLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        viewModel.fetchCoursesbyCoach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
}