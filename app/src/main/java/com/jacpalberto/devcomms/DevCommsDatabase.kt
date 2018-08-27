package com.jacpalberto.devcomms

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.sponsors.SponsorsDao

/**
 * Created by Alberto Carrillo on 8/20/18.
 */
@Database(entities = [(Sponsor::class)], version = 1)
abstract class DevCommsDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: DevCommsDatabase? = null

        fun getInstance(context: Context): DevCommsDatabase {
            if (INSTANCE == null) {
                synchronized(DevCommsDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            DevCommsDatabase::class.java, "devComms")
                            .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    abstract fun sponsorsDao(): SponsorsDao
}