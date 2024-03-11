package com.yanncha.timeslotpro.network.database

import androidx.room.Database
import androidx.room.Ignore
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yanncha.timeslotpro.network.dao.CoachDao
import com.yanncha.timeslotpro.network.dao.CoursDao
import com.yanncha.timeslotpro.network.dao.CourseWithCoachDetailsDao
import com.yanncha.timeslotpro.network.dao.ReservationDao
import com.yanncha.timeslotpro.network.dao.UserDao
import com.yanncha.timeslotpro.network.models.Coach
import com.yanncha.timeslotpro.network.models.Cours
import com.yanncha.timeslotpro.network.models.CourseWithCoachDetails
import com.yanncha.timeslotpro.network.models.Reservation
import com.yanncha.timeslotpro.network.models.User
import com.yanncha.timeslotpro.util.Converters

@Database(entities = [Coach::class, Cours::class, Reservation::class, User::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coachDao(): CoachDao
    abstract fun coursDao(): CoursDao
    abstract fun reservationDao(): ReservationDao
    abstract fun userDao(): UserDao
    @Ignore
    abstract fun courseWithCoachDetailsDao() : CourseWithCoachDetailsDao
}