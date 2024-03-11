package com.yanncha.timeslotpro.ui.user.register

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.network.dao.CoachDao
import com.yanncha.timeslotpro.network.dao.UserDao
import com.yanncha.timeslotpro.network.models.Coach
import com.yanncha.timeslotpro.network.models.User
import com.yanncha.timeslotpro.util.toTitleCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    private val userDao: UserDao
) : ViewModel() {

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData

    private fun validateInput(password: String, confirmPassword: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
        if (!passwordRegex.matches(password)) {
            _userMessageLiveData.value = R.string.invalid_password
            return false
        }
        if (password != confirmPassword) {
            _userMessageLiveData.value = R.string.login_password_mismatch
            return false
        }
        return true
    }

    fun checkFieldsAndSignUp(
        nom: String,
        prenom: String,
        identifiant: String,
        password: String,
        confirmPassword: String,
        urlImage: String
    ) {
        if (nom.isBlank() || prenom.isBlank() || identifiant.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _userMessageLiveData.value = R.string.fields_not_filled
            return
        }
        if (validateInput(password, confirmPassword)) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (userDao.findUserByIdentifiant(identifiant) != null) {
                        _userMessageLiveData.postValue(R.string.identifiant_already_exists)
                    } else {
                        User(
                            nom = nom.toTitleCase().trim(),
                            prenom = prenom.toTitleCase().trim(),
                            identifiant = identifiant.trim(),
                            mdp = password,
                            url_image = urlImage.trim()
                        ).let {
                            userDao.insertUser(it)
                            _userMessageLiveData.postValue(R.string.registration_successful)
                            myPrefs.edit()?.apply {
                                putLong("id", userDao.getUserIdByLogin(identifiant)!!)
                                putString("niveau", "Débutant")
                                apply()
                            }
                        }
                    }
                }
            }
        }
    }
}