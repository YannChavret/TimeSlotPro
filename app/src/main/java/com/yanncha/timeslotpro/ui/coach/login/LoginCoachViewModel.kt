package com.yanncha.timeslotpro.ui.coach.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.network.dao.CoachDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginCoachViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    private val coachDao: CoachDao
) : ViewModel() {

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData



    fun checkFieldsAndLogIn(identifiant: String, mdp: String) {
        if (identifiant.isNotBlank() && mdp.isNotBlank()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    coachDao.loginCoach(identifiant, mdp).let {
                        withContext(Dispatchers.Main) {
                            if (it != null) {
                                _userMessageLiveData.value = R.string.welcome_user
                                myPrefs.edit().apply {
                                    putString("role","coach")
                                    putLong("id", it.id)
                                    apply()
                                }
                            } else {
                                _userMessageLiveData.value = R.string.invalid_login
                            }
                        }
                    }
                }
            }
        } else {
            _userMessageLiveData.value = R.string.fields_not_filled
        }
    }
}