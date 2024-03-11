package com.yanncha.timeslotpro.network.dao

import androidx.room.Dao
import androidx.room.Query
import com.yanncha.timeslotpro.network.models.CourseWithCoachDetails

@Dao
interface CourseWithCoachDetailsDao {
    @Query("""
        SELECT C.id AS courseId, C.intitule, C.niveau, C.date, C.heure_debut, C.heure_fin, C.limite_participants, 
        CO.nom AS nomCoach, CO.prenom AS prenomCoach, CO.url_image AS urlImageCoach, 
        (SELECT COUNT(*) FROM Reservation WHERE id_cours = C.id) AS nbInscrits
        FROM Cours C
        INNER JOIN Coach CO ON C.id_coach = CO.id
        ORDER BY date ASC""")
    suspend fun getCoursesWithCoachDetails(): List<CourseWithCoachDetails>

    @Query("""
        SELECT C.id AS courseId, C.intitule, C.niveau, C.date, C.heure_debut, C.heure_fin, C.limite_participants, 
        CO.nom AS nomCoach, CO.prenom AS prenomCoach, CO.url_image AS urlImageCoach, 
        (SELECT COUNT(*) FROM Reservation WHERE id_cours = C.id) AS nbInscrits
        FROM Cours C
        INNER JOIN Coach CO ON C.id_coach = CO.id
        WHERE C.id = :courseId""")
    suspend fun getCourseWithCoachDetails(courseId: Long): CourseWithCoachDetails?

    @Query("""
        SELECT C.id AS courseId, C.intitule, C.niveau, C.date, C.heure_debut, C.heure_fin, C.limite_participants, 
        CO.nom AS nomCoach, CO.prenom AS prenomCoach, CO.url_image AS urlImageCoach, 
        (SELECT COUNT(*) FROM Reservation WHERE id_cours = C.id) AS nbInscrits
        FROM Cours C
        INNER JOIN Coach CO ON C.id_coach = CO.id
        WHERE CO.id = :coachId
        ORDER BY C.date ASC""")
    suspend fun getCourseByCoachWithUsers(coachId: Long): List<CourseWithCoachDetails>
}

