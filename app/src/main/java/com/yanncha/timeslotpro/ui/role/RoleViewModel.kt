package com.yanncha.timeslotpro.ui.role

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RoleViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
): ViewModel() {


}