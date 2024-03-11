package com.yanncha.timeslotpro.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(myPrefs: SharedPreferences) : ViewModel() {
    private val _myPrefsLiveData = MutableLiveData<SharedPreferences>(myPrefs)
    val myPrefsLiveData: LiveData<SharedPreferences>
        get() = _myPrefsLiveData

}