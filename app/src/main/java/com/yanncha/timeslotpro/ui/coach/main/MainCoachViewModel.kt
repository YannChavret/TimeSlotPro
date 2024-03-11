package com.yanncha.timeslotpro.ui.coach.main

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.network.dao.CoursDao
import com.yanncha.timeslotpro.network.dao.CourseWithCoachDetailsDao
import com.yanncha.timeslotpro.network.models.CourseWithCoachDetails
import com.yanncha.timeslotpro.util.Event
import com.yanncha.timeslotpro.util.ReservationFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MainCoachViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    private val coursDao: CourseWithCoachDetailsDao
) : ViewModel() {

    private val _coursesLiveData = MutableLiveData(listOf<CourseWithCoachDetails>())
    val coursesLiveData: LiveData<List<CourseWithCoachDetails>>
        get() = _coursesLiveData

    private val _selectedCourseLiveData = MutableLiveData<Event<CourseWithCoachDetails>>()
    val selectedCourseLiveData: LiveData<Event<CourseWithCoachDetails>>
        get() = _selectedCourseLiveData

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData

    init {
        fetchCoursesbyCoach()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchCoursesbyCoach() {
        val userId = myPrefs.getLong("id",-1)
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val coursesList = withContext(Dispatchers.IO) {
            coursDao.getCourseByCoachWithUsers(userId).sortedBy {
                LocalDate.parse(it.date, formatter)
            }
        }
            _coursesLiveData.value = coursesList
            if(coursesList.isEmpty())
                _userMessageLiveData.value = R.string.no_courses_available
        }
    }

    fun onCourseSelected(course: CourseWithCoachDetails) {
        _selectedCourseLiveData.value = Event(course)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterCourses(filter: ReservationFilter) {
        val userId = myPrefs.getLong("id", -1)
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val today = LocalDate.now()

            val allCourses = withContext(Dispatchers.IO) {
                coursDao.getCourseByCoachWithUsers(userId)
            }

            val filteredCourses = when (filter) {
                ReservationFilter.ALL -> allCourses
                ReservationFilter.FUTURE -> allCourses.filter {
                    it.date.let { date ->
                        LocalDate.parse(date, formatter).isAfter(today)
                    }
                }.sortedBy {
                    it.date.let { date -> LocalDate.parse(date, formatter) }
                }
                ReservationFilter.PAST -> allCourses.filter {
                    it.date.let { date ->
                        LocalDate.parse(date, formatter).isBefore(today) || LocalDate.parse(date, formatter).isEqual(today)
                    }
                }.sortedByDescending {
                    it.date.let { date -> LocalDate.parse(date, formatter) }
                }
            }
            _coursesLiveData.value = filteredCourses
        }
    }

}