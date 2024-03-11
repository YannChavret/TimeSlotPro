package com.yanncha.timeslotpro.network.models

data class CourseWithCoachDetails(
    val courseId: Long,
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
