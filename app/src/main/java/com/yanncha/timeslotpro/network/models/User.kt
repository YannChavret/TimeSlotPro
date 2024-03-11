package com.yanncha.timeslotpro.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nom: String,
    val prenom: String,
    val identifiant: String,
    val mdp: String,
    val url_image : String,
    var niveau: String = "Débutant",
    var credit: Int = 4
)