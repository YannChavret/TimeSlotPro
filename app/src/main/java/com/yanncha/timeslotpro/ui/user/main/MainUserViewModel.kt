package com.yanncha.timeslotpro.ui.user.main

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.network.dao.ReservationDao
import com.yanncha.timeslotpro.network.models.ReservationWithCourseDetails
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
class MainUserViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    private val reservationDao: ReservationDao
) : ViewModel() {

    private val _reservationsLiveData = MutableLiveData(listOf<ReservationWithCourseDetails?>())
    val reservationsLiveData: LiveData<List<ReservationWithCourseDetails?>>
        get() = _reservationsLiveData

    private val _selectedReservationLiveData = MutableLiveData<Event<ReservationWithCourseDetails>>()
    val selectedReservationLiveData: LiveData<Event<ReservationWithCourseDetails>>
        get() = _selectedReservationLiveData

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData

    init {
        fetchReservationByUser()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchReservationByUser() {
        val userId = myPrefs.getLong("id", -1)
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val reservationsList = withContext(Dispatchers.IO) {
                reservationDao.getReservationsWithCourseDetails(userId).sortedBy {
                    LocalDate.parse(it?.date, formatter)
                }
            }
            _reservationsLiveData.value = reservationsList
            if (reservationsList.isEmpty())
                _userMessageLiveData.value = R.string.no_reservation_available
        }
    }

    fun onReservationSelected(reservation: ReservationWithCourseDetails) {
        _selectedReservationLiveData.value = Event(reservation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterReservations(filter: ReservationFilter) {
        val userId = myPrefs.getLong("id", -1)
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val today = LocalDate.now()

            val allReservations = withContext(Dispatchers.IO) {
                reservationDao.getReservationsWithCourseDetails(userId)
            }

            val filteredReservations = when (filter) {
                ReservationFilter.ALL -> allReservations
                ReservationFilter.FUTURE -> allReservations.filter {
                    it?.date?.let { date ->
                        LocalDate.parse(date, formatter).isAfter(today)
                    } ?: false
                }.sortedBy {
                    it?.date?.let { date -> LocalDate.parse(date, formatter) }
                }
                ReservationFilter.PAST -> allReservations.filter {
                    it?.date?.let { date ->
                        LocalDate.parse(date, formatter).isBefore(today) || LocalDate.parse(date, formatter).isEqual(today)
                    } ?: false
                }.sortedByDescending {
                    it?.date?.let { date -> LocalDate.parse(date, formatter) }
                }
            }
            _reservationsLiveData.value = filteredReservations
        }
    }
}