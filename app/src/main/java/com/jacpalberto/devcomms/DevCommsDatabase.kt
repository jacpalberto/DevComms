package com.jacpalberto.devcomms

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.jacpalberto.devcomms.data.DateConverter
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.events.EventsDao
import com.jacpalberto.devcomms.sponsors.SponsorsDao

/**
 * Created by Alberto Carrillo on 8/20/18.
 */
@Database(entities = [(Sponsor::class), (DevCommsEvent::class)], version = 1)
@TypeConverters(DateConverter::class)
abstract class DevCommsDatabase : RoomDatabase() {
    abstract fun sponsorsDao(): SponsorsDao
    abstract fun eventsDao(): EventsDao
}