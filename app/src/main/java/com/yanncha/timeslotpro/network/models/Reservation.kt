package com.yanncha.timeslotpro.network.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Cours::class,
            parentColumns = ["id"],
            childColumns = ["id_cours"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["id_user"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Reservation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val id_cours: Long,
    val id_user: Long,
    val date_creation: String
)
