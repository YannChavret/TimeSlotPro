package com.yanncha.timeslotpro.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yanncha.timeslotpro.network.models.Cours


@Dao
interface CoursDao {
    @Insert
    suspend fun insertCourse(cours: Cours): Long

    @Query("SELECT * FROM Cours WHERE date >= :currentDate ORDER BY date ASC")
    suspend fun getUpcomingCourses(currentDate: String): List<Cours>

    @Query("SELECT * FROM Cours WHERE date < :currentDate ORDER BY date DESC")
    suspend fun getPastCourses(currentDate: String): List<Cours>

    @Query("SELECT * FROM Cours WHERE id_coach = :coachId ORDER BY date ASC")
    suspend fun getCoursesByCoach(coachId: Long): List<Cours>

}
