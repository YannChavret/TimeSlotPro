package com.yanncha.timeslotpro.ui.coach

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.network.dao.CoachDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoachViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    ) : ViewModel() {

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData

    fun logOut() {
        myPrefs.edit().clear().apply()
        _userMessageLiveData.value = R.string.logout_user
    }
}