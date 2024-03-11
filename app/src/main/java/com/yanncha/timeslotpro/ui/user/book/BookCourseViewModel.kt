package com.yanncha.timeslotpro.ui.user.book

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.network.dao.CourseWithCoachDetailsDao
import com.yanncha.timeslotpro.network.models.CourseWithCoachDetails
import com.yanncha.timeslotpro.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class BookCourseViewModel @Inject constructor(
    private val courseWithCoachDetailsDao: CourseWithCoachDetailsDao
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
        fetchAllCourses()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchAllCourses() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val coursesList = withContext(Dispatchers.IO) {
                courseWithCoachDetailsDao.getCoursesWithCoachDetails().filter { course ->
                    LocalDate.parse(course.date, formatter).isAfter(today) ||
                            LocalDate.parse(course.date, formatter).isEqual(today)
                }
            }
            _coursesLiveData.value = coursesList
            if (coursesList.isEmpty())
                _userMessageLiveData.value = R.string.no_courses_available

        }
    }

    fun onCourseSelected(course: CourseWithCoachDetails) {
        _selectedCourseLiveData.value = Event(course)
    }
}