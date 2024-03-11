package com.yanncha.timeslotpro.ui.user.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.network.dao.CoachDao
import com.yanncha.timeslotpro.network.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginUserViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    private val userDao: UserDao
) : ViewModel() {

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData



    fun checkFieldsAndLogIn(identifiant: String, mdp: String) {
        if (identifiant.isNotBlank() && mdp.isNotBlank()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    userDao.loginUser(identifiant, mdp).let {
                        withContext(Dispatchers.Main) {
                            if (it != null) {
                                _userMessageLiveData.value = R.string.welcome_user
                                myPrefs.edit().apply {
                                    putString("role","user")
                                    putLong("id", it.id)
                                    putInt("credits", it.credit)
                                    putString("niveau", it.niveau)
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