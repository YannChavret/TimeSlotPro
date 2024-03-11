package com.yanncha.timeslotpro.ui.user.book_details

import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.network.dao.CourseWithCoachDetailsDao
import com.yanncha.timeslotpro.network.dao.ReservationDao
import com.yanncha.timeslotpro.network.dao.UserDao
import com.yanncha.timeslotpro.network.models.Cours
import com.yanncha.timeslotpro.network.models.CourseWithCoachDetails
import com.yanncha.timeslotpro.network.models.Reservation
import com.yanncha.timeslotpro.network.models.User
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BookDetailsCourseViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    private val courseWithCoachDetailsDao: CourseWithCoachDetailsDao,
    private val userDao: UserDao,
    private val reservationDao: ReservationDao
) : ViewModel() {

    private val _courseDetailLiveData = MutableLiveData<CourseWithCoachDetails>()
    val courseDetailLiveData: LiveData<CourseWithCoachDetails>
        get() = _courseDetailLiveData

    private val _isExistingReservationLiveData = MutableLiveData<Boolean>()
    val isExistingReservationLiveData: LiveData<Boolean>
        get() = _isExistingReservationLiveData

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

    private val userId = myPrefs.getLong("id", -1)

    init {

        viewModelScope.launch {
            val userId = userId
            val courseId = courseId

            checkReservationStatus(userId, courseId)
        }
    }

    private fun checkReservationStatus(userId: Long, courseId: Long) {
        viewModelScope.launch {
            val existingReservation = getExistingReservation(userId, courseId)
            _isExistingReservationLiveData.postValue(existingReservation != null)
        }
    }

    fun fetchCourseById(args: BookDetailsCourseFragmentArgs) {
        courseId = args.courseId

        viewModelScope.launch {
            val course = withContext(Dispatchers.IO) {
                courseWithCoachDetailsDao.getCourseWithCoachDetails(courseId)
            }
            _courseDetailLiveData.value = course!!
            checkReservationStatus(userId, courseId)

        }

    }

    private suspend fun getExistingReservation(userId: Long, courseId: Long): Reservation? {
        return withContext(Dispatchers.IO) {
            val existingReservation = reservationDao.getReservationByUserAndCourse(userId, courseId)
            withContext(Dispatchers.Main) {
                _isExistingReservationLiveData.postValue(existingReservation != null)
            }
            existingReservation
        }
    }


    private suspend fun checkAndBookReservation(reservation: Reservation) {
        val existingReservation = getExistingReservation(reservation.id_user, reservation.id_cours)
        if (existingReservation == null) {
            val user = userDao.getUserById(reservation.id_user)
            if (user != null && user.credit > 0) {
                user.credit -= 1
                userDao.updateUser(user)
                val reservationId = reservationDao.insertReservation(reservation)
                val insertedReservation = reservationDao.getReservationById(reservationId)
                if (insertedReservation != null) {
                    _userMessageLiveData.postValue(R.string.course_booked)
                } else {
                    _userMessageLiveData.postValue(R.string.booking_error)
                }
            } else {
                _userMessageLiveData.postValue(R.string.insufficient_credits)
            }
        } else {
            _userMessageLiveData.postValue(R.string.already_booked)
        }
    }


    fun bookCourse() {
        viewModelScope.launch {
            val userLevel = myPrefs.getString("niveau", "")
            val currentTimeMillis = System.currentTimeMillis()
            val dateCreation = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                Date(currentTimeMillis)
            )
            val reservation = Reservation(
                id_cours = courseId,
                id_user = userId,
                date_creation = dateCreation
            )
            if (userLevel == courseDetailLiveData.value?.niveau) {
                checkAndBookReservation(reservation)
            } else {
                _userMessageLiveData.postValue(R.string.level_mismatch)
            }
            _operationCompleted.postValue(true)
        }
    }


    fun fetchUsersByCourse(args: BookDetailsCourseFragmentArgs) {
        courseId = args.courseId

        viewModelScope.launch {
            val users = withContext(Dispatchers.IO) {
                userDao.getUsersByBookedCourse(courseId)
            }
            _usersListLiveData.value = users
        }
    }

    fun cancelReservation(courseId: Long) {
        viewModelScope.launch {
            val reservation = getExistingReservation(userId, courseId)
            if (reservation != null) {
                val course = courseWithCoachDetailsDao.getCourseWithCoachDetails(courseId)

                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val courseDate = formatter.parse(course?.date ?: "")
                val currentDate = Date()

                if (courseDate != null && courseDate.before(currentDate) || courseDate.equals(currentDate)) {
                    _userMessageLiveData.postValue(R.string.cannot_cancel_past_reservation)
                    _operationCompleted.postValue(true)
                    return@launch
                }

                val user = userDao.getUserById(userId)
                if (user != null) {
                    user.credit += 1
                    userDao.updateUser(user)
                    reservationDao.deleteReservationById(reservation.id)
                    _userMessageLiveData.postValue(R.string.reservation_cancelled)
                } else {
                    _userMessageLiveData.postValue(R.string.user_not_found)
                }
            } else {
                _userMessageLiveData.postValue(R.string.reservation_not_found)
            }
            _operationCompleted.postValue(true)
        }
    }

    fun resetOperationCompleted() {
        _operationCompleted.value = false
    }
}