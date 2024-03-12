package com.yanncha.timeslotpro.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.yanncha.timeslotpro.network.models.Reservation
import com.yanncha.timeslotpro.network.models.ReservationWithCourseDetails

@Dao
interface ReservationDao {
    @Insert
    suspend fun insertReservation(reservation: Reservation): Long

    @Query("SELECT * FROM Reservation WHERE id_cours = :coursId")
    suspend fun getReservationsByCourse(coursId: Long): List<Reservation>

    @Query("SELECT * FROM Reservation WHERE id_user = :userId")
    suspend fun getReservationsByUser(userId: Long): List<Reservation>

    @Query("SELECT COUNT(*) FROM Reservation WHERE id_cours = :coursId")
    suspend fun getReservationCountForCourse(coursId: Long): Int

    @Query("SELECT * FROM Reservation WHERE id_user = :userId AND id_cours = :courseId")
    suspend fun getReservationByUserAndCourse(userId: Long, courseId: Long): Reservation?

    @Query("SELECT * FROM Reservation WHERE id = :reservationId")
    suspend fun getReservationById(reservationId: Long): Reservation?

    @Query("DELETE FROM Reservation WHERE id = :reservationId")
    suspend fun deleteReservationById(reservationId: Long)

    @Transaction
    @Query("""
    SELECT R.*, C.intitule, C.niveau, C.date, C.heure_debut, C.heure_fin, C.limite_participants, 
    CO.nom AS nomCoach, CO.prenom AS prenomCoach, CO.url_image AS urlImageCoach,
    (SELECT COUNT(*) FROM Reservation WHERE id_cours = C.id) AS nbInscrits
    FROM Reservation R
    INNER JOIN Cours C ON R.id_cours = C.id
    INNER JOIN Coach CO ON C.id_coach = CO.id
    WHERE R.id_user = :userId""")
    suspend fun getReservationsWithCourseDetails(userId: Long): List<ReservationWithCourseDetails?>


}