package com.jacpalberto.devcomms

import android.app.Application
import androidx.room.Room

/**
 * Created by Alberto Carrillo on 8/24/18.
 */
class DevCommsApp : Application() {
    companion object {
        var database: DevCommsDatabase? = null
    }

    //TODO: remove MainThreadQueriesAllowed
    override fun onCreate() {
        super.onCreate()
        DevCommsApp.database = Room.databaseBuilder(this,
                DevCommsDatabase::class.java,
                BuildConfig.roomDbName)
                .allowMainThreadQueries()
                .build()
    }
}