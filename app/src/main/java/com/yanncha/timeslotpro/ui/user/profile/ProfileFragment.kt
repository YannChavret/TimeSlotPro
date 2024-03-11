package com.yanncha.timeslotpro.ui.user.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.databinding.FragmentBookDetailsCourseBinding
import com.yanncha.timeslotpro.databinding.FragmentProfileBinding
import com.yanncha.timeslotpro.ui.user.book_details.BookDetailsCourseViewModel
import com.yanncha.timeslotpro.util.picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        with(binding){

            viewModel.userInfosLiveData.observe(viewLifecycleOwner){user->
                picasso(user?.url_image, ivProfileAvatar)
                tvProfileUsername.text= getString(R.string.username_profile, user?.prenom, user?.nom)
                tvProfileCreditsCount.text= user?.credit.toString()
                tvProfileLevelUser.text=user?.niveau
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}