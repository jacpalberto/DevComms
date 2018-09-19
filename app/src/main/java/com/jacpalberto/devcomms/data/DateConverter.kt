package com.jacpalberto.devcomms.data

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Created by Alberto Carrillo on 9/19/18.
 */
class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}