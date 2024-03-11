package com.yanncha.timeslotpro.network.models

data class ReservationWithCourseDetails(
    val id: Long,
    val id_cours: Long,
    val id_user: Long,
    val date_creation: String,
    val intitule: String,
    val niveau: String,
    val date: String,
    val heure_debut: String,
    val heure_fin: String,
    val limite_participants: Int,
    val nbInscrits: Int,
    val nomCoach: String,
    val prenomCoach: String,
    val urlImageCoach: String
)
