package com.yanncha.timeslotpro.ui.coach.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentCreateCourseBinding
import com.yanncha.timeslotpro.util.setFocusChangeBlue
import com.yanncha.timeslotpro.util.setFocusChangePink
import com.yanncha.timeslotpro.util.setFocusChangeYellow
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class CreateCourseFragment : Fragment() {

    private val viewModel: CreateCourseViewModel by viewModels()
    private var _binding: FragmentCreateCourseBinding? = null
    private val binding: FragmentCreateCourseBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateCourseBinding.inflate(inflater, container, false)

        with(binding) {
            etTheme.setFocusChangeBlue()
            //etNiveau.setFocusChangeYellow()
            etNbLimit.setFocusChangePink()
            etDate.setFocusChangeBlue()
            etHeureDebut.setFocusChangeYellow()
            etHeureFin.setFocusChangePink()

            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.niveau_array,
                R.layout.spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_dropdown)
                binding.etNiveau.adapter = adapter
            }


            etDate.setOnClickListener {
                Calendar.getInstance().let { now ->
                    DatePickerDialog(requireContext(), { _, year, month, day ->
                        Calendar.getInstance().apply {
                            set(year, month, day)
                        }.let { selectedDate ->
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(selectedDate.time)
                                .let { formattedDate ->
                                etDate.setText(formattedDate)
                            }
                        }
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                        .apply {
                        datePicker.minDate = System.currentTimeMillis()
                        show()
                    }
                }
            }

            etHeureDebut.setOnClickListener {
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    etHeureDebut.setText(
                        String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            hour,
                            minute
                        )
                    )
                }, 12, 0, true).apply {
                    updateTime(12, 0)
                    show()
                }
            }

            etHeureFin.setOnClickListener {
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    etHeureFin.setText(
                        String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            hour,
                            minute
                        )
                    )
                }, 12, 0, true).apply {
                    updateTime(12, 0)
                    show()
                }
            }

            btnCreate.setOnClickListener{
                val selectedNiveau = etNiveau.selectedItem.toString()

                viewModel.checkFieldsAndAddCourse(
                    etTheme.text.toString(),
                    selectedNiveau,
                    etNbLimit.text.toString(),
                    etDate.text.toString(),
                    etHeureDebut.text.toString(),
                    etHeureFin.text.toString()
                )
            }
        }

        viewModel.userMessageLiveData.observe(viewLifecycleOwner) {
            if(it==R.string.course_added_successfully){
                findNavController().popBackStack()
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
