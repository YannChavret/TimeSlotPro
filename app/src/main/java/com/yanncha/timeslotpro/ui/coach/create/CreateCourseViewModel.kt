package com.yanncha.timeslotpro.ui.coach.create

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanncha.timeslotpro.R
import com.yanncha.timeslotpro.network.dao.CoursDao
import com.yanncha.timeslotpro.network.models.Cours
import com.yanncha.timeslotpro.util.toTitleCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val myPrefs: SharedPreferences,
    private val coursDao: CoursDao
) : ViewModel() {

    private val _userMessageLiveData = MutableLiveData<Int>()
    val userMessageLiveData: LiveData<Int>
        get() = _userMessageLiveData

    private var coachId: Long = -1


    fun checkFieldsAndAddCourse(theme: String, niveau: String, nbLimit: String, date: String, heureDebut: String, heureFin: String) {

        if (theme.isBlank() || niveau.isBlank() || nbLimit.isBlank() || date.isBlank() || heureDebut.isBlank() || heureFin.isBlank()) {
            _userMessageLiveData.value = R.string.fields_not_filled
            return
        }

        if (niveau != "Débutant" && niveau != "Confirmé") {
            _userMessageLiveData.value = R.string.invalid_niveau
            return
        }

        coachId = myPrefs.getLong("id", -1)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Cours(
                    intitule = theme.toTitleCase(),
                    niveau = niveau,
                    date = date,
                    heure_debut = heureDebut,
                    heure_fin = heureFin,
                    limite_participants = nbLimit.toInt(),
                    id_coach = coachId
                ).let {
                    coursDao.insertCourse(it)
                    _userMessageLiveData.postValue(R.string.course_added_successfully)
                }
            }
        }
    }

}