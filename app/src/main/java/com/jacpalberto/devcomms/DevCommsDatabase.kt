package com.jacpalberto.devcomms

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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