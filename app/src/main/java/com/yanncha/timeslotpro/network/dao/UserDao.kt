package com.yanncha.timeslotpro.network.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yanncha.timeslotpro.network.models.User


@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM User WHERE identifiant = :identifiant")
    suspend fun findUserByIdentifiant(identifiant: String): User?

    @Query("SELECT id FROM User WHERE identifiant = :identifiant")
    suspend fun getUserIdByLogin(identifiant: String): Long?

    @Query("SELECT * FROM User WHERE identifiant = :identifiant AND mdp = :mdp")
    suspend fun loginUser(identifiant: String, mdp: String): User?

    @Query("SELECT U.* FROM User U INNER JOIN Reservation R ON U.id = R.id_user WHERE R.id_cours = :coursId")
    suspend fun getUsersByBookedCourse(coursId: Long): List<User>

    @Query("SELECT * FROM User WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getUserByIdLiveData(userId: Long): LiveData<User>


    @Update
    suspend fun updateUser(user: User)
}
