package com.yanncha.timeslotpro.network.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/*data class CoursAvecParticipants(
    @Embedded val cours: Cours,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_user",
        associateBy = Junction(Reservation::class)
    )
    val participants: List<User>
)*/