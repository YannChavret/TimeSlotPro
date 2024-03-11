package com.yanncha.timeslotpro.ui.coach.students

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentUserEditDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserEditDialogFragment : DialogFragment() {

    private val args: UserEditDialogFragmentArgs by navArgs()
    private val viewModel: StudentsViewModel by activityViewModels()
    private var _binding: FragmentUserEditDialogBinding? = null
    private val binding: FragmentUserEditDialogBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserEditDialogBinding.inflate(inflater, container, false)


        with(binding) {
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.niveau_array,
                R.layout.spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_dropdown)
                binding.etEditNiveau.adapter = adapter
            }

            tvEditUsername.text =
                getString(R.string.username, args.userFirstname, args.userLastname)

            btnEdit.setOnClickListener {
                val selectedNiveau = etEditNiveau.selectedItem.toString()
                val selectedCredits = if (etEditCredits.text.toString().isNotEmpty())
                    etEditCredits.text.toString().toInt() else 0
                viewModel.updateUser(args.userId, selectedNiveau, selectedCredits)
                dismiss()
                viewModel.refresh()
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}