package com.yanncha.timeslotpro.network.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Coach::class,
            parentColumns = ["id"],
            childColumns = ["id_coach"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id_coach")]
)
data class Cours(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val intitule: String,
    val niveau: String,
    val date: String,
    val heure_debut: String,
    val heure_fin: String,
    val limite_participants: Int,
    val id_coach: Long
)