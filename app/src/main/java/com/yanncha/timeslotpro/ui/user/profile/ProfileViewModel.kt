package com.yanncha.timeslotpro.ui.user.profile

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.network.dao.UserDao
import com.yanncha.timeslotpro.network.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    private val userDao: UserDao,
) : ViewModel() {

    private val userId = myPrefs.getLong("id", -1)

    val userInfosLiveData: LiveData<User> = userDao.getUserByIdLiveData(userId)

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData




    /*init {
        getUserInfos()
    }

    fun getUserInfos() {
        viewModelScope.launch{
            val user = withContext(Dispatchers.IO){
                userDao.getUserById(userId)
            }
            _userInfosLiveData.value = user
        }
    }*/


}