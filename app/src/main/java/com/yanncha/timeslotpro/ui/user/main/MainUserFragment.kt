package com.yanncha.timeslotpro.ui.user.main

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
import com.yanncha.timeslotpro.databinding.FragmentMainUserBinding
import com.yanncha.timeslotpro.util.ReservationFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainUserFragment : Fragment() {

    private val viewModel: MainUserViewModel by viewModels()
    private var _binding: FragmentMainUserBinding? = null
    private val binding: FragmentMainUserBinding
        get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainUserBinding.inflate(inflater, container, false)

        val reservationsAdapter = ReservationListAdapter(viewModel)
        var filter:ReservationFilter = ReservationFilter.ALL

        with(binding) {

            rvMainUser.apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = reservationsAdapter
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.filterReservations(filter)
                swipeRefreshLayout.isRefreshing = false
                rvMainUser.layoutManager?.scrollToPosition(0)
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
                        viewModel.filterReservations(filter)
                    }
            }
        }

        viewModel.reservationsLiveData.observe(viewLifecycleOwner) { reservation ->
            reservationsAdapter.submitList(reservation)
            binding.rvMainUser.layoutManager?.scrollToPosition(0)
        }

        viewModel.selectedReservationLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { reservation ->
                findNavController().navigate(MainUserFragmentDirections.actionMainUserFragmentToBookDetailsCourseFragment(reservation.id_cours))
            }
        }

        viewModel.userMessageLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchReservationByUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
}