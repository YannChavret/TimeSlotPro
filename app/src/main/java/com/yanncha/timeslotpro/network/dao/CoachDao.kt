package com.yanncha.timeslotpro.network.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yanncha.timeslotpro.network.models.Coach
import com.yanncha.timeslotpro.network.models.Cours
import com.yanncha.timeslotpro.network.models.User

@Dao
interface CoachDao {
    @Insert
    suspend fun insertCoach(coach: Coach): Long

    @Query("SELECT * FROM Coach WHERE identifiant = :identifiant")
    suspend fun findCoachByIdentifiant(identifiant: String): Coach?

    @Query("SELECT id FROM Coach WHERE identifiant = :identifiant")
    suspend fun getCoachIdByLogin(identifiant: String): Long?

    @Query("SELECT * FROM Coach WHERE identifiant = :identifiant AND mdp = :mdp")
    suspend fun loginCoach(identifiant: String, mdp: String): Coach?

    @Query("SELECT * FROM Cours WHERE id_coach = :coachId")
    suspend fun getCoursesByCoach(coachId: Int): List<Cours>

    @Query("SELECT * FROM User")
    suspend fun getAllStudents(): List<User>

    @Query("SELECT * FROM Coach WHERE id = :coachId")
    fun getCoachByIdLiveData(coachId: Long): LiveData<Coach>

    @Query("""
        SELECT DISTINCT U.* FROM User U
        INNER JOIN Reservation R ON U.id = R.id_user
        INNER JOIN Cours C ON R.id_cours = C.id
        WHERE C.id_coach = :coachId
    """)
    suspend fun getUsersByCoach(coachId: Long): List<User>

    @Update
    suspend fun updateStudent(user: User)

}