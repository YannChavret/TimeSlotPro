package com.yanncha.timeslotpro.network.database

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "timeslotpro.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCoachDao(database: AppDatabase) = database.coachDao()

    @Provides
    fun provideCoursDao(database: AppDatabase) = database.coursDao()

    @Provides
    fun provideReservationDao(database: AppDatabase) = database.reservationDao()

    @Provides
    fun provideUsersDao(database: AppDatabase) = database.userDao()

    @Provides
    fun provideCoursesWithCoachDao(database: AppDatabase) = database.courseWithCoachDetailsDao()

    @Singleton
    @Provides
    fun provideMyPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("timeslotpro", Context.MODE_PRIVATE)
    }
}