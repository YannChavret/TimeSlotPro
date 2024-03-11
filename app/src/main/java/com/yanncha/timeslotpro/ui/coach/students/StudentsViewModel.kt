package com.yanncha.timeslotpro.ui.coach.students

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.network.dao.CoachDao
import com.yanncha.timeslotpro.network.dao.UserDao
import com.yanncha.timeslotpro.network.models.Coach
import com.yanncha.timeslotpro.network.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    private val coachDao: CoachDao,
    private val userDao: UserDao,
) : ViewModel() {

    private val coachId = myPrefs.getLong("id", -1)

    val coachInfosLiveData: LiveData<Coach> = coachDao.getCoachByIdLiveData(coachId)

    private val _usersListLiveData = MutableLiveData(listOf<User>())
    val usersListLiveData: LiveData<List<User>>
        get() = _usersListLiveData

    private val _operationCompleted = MutableLiveData<Boolean>()
    val operationCompleted: LiveData<Boolean>
        get() = _operationCompleted

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData

    init {
        fetchUsersByCoach(coachId)
    }

    fun fetchUsersByCoach(coachId: Long){

        viewModelScope.launch {
            val users = withContext(Dispatchers.IO) {
                coachDao.getUsersByCoach(coachId)
            }
            _usersListLiveData.value = users
        }
    }

    fun updateUser(userId: Long, niveau: String, credits: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = userDao.getUserById(userId)
                user?.let {
                    if((niveau=="Débutant") || (niveau=="Confirmé")) {
                        it.niveau = niveau
                    }
                    it.credit += credits
                    userDao.updateUser(it)
                    refresh()
                }
            }
        }
    }

    fun resetOperationCompleted() {
        _operationCompleted.value = false
    }

    fun refresh(){
        fetchUsersByCoach(coachId)
    }
}