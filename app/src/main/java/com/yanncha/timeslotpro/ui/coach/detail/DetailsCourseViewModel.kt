package com.yanncha.timeslotpro.ui.coach.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.network.dao.CourseWithCoachDetailsDao
import com.yanncha.timeslotpro.network.dao.UserDao
import com.yanncha.timeslotpro.network.models.CourseWithCoachDetails
import com.yanncha.timeslotpro.network.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsCourseViewModel @Inject constructor(
    private val courseWithCoachDetailsDao: CourseWithCoachDetailsDao,
    private val userDao: UserDao
) : ViewModel() {

    private val _courseDetailLiveData = MutableLiveData<CourseWithCoachDetails>()
    val courseDetailLiveData: LiveData<CourseWithCoachDetails>
        get() = _courseDetailLiveData

    private val _usersListLiveData = MutableLiveData(listOf<User>())
    val usersListLiveData: LiveData<List<User>>
        get() = _usersListLiveData

    private val _operationCompleted = MutableLiveData<Boolean>()
    val operationCompleted: LiveData<Boolean>
        get() = _operationCompleted

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData

    private var courseId: Long = -1


    fun fetchCourseById(args: DetailsCourseFragmentArgs) {
        courseId = args.courseId

        viewModelScope.launch {
            val course = withContext(Dispatchers.IO) {
                courseWithCoachDetailsDao.getCourseWithCoachDetails(courseId)
            }
            _courseDetailLiveData.value = course!!

        }

    }

    fun fetchUsersByCourse(args: DetailsCourseFragmentArgs) {
        courseId = args.courseId

        viewModelScope.launch {
            val users = withContext(Dispatchers.IO) {
                userDao.getUsersByBookedCourse(courseId)
            }
            _usersListLiveData.value = users
        }
    }

    fun resetOperationCompleted() {
        _operationCompleted.value = false
    }
}