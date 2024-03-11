package com.yanncha.timeslotpro.util

import android.icu.text.SimpleDateFormat
import androidx.room.TypeConverter
import java.util.Date
import java.util.Locale

class Converters {
    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let { format.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.let { format.format(it) }
    }
}