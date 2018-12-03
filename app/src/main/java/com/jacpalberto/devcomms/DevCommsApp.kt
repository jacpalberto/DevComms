package com.jacpalberto.devcomms

import android.app.Application
import androidx.room.Room
import com.jacpalberto.devcomms.di.addressModule
import com.jacpalberto.devcomms.di.eventsModule
import com.jacpalberto.devcomms.di.generalModule
import com.jacpalberto.devcomms.di.sponsorsModule
import org.koin.android.ext.android.startKoin

/**
 * Created by Alberto Carrillo on 8/24/18.
 */
class DevCommsApp : Application() {
    companion object {
        var database: DevCommsDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(sponsorsModule, eventsModule, generalModule, addressModule))
        DevCommsApp.database = Room.databaseBuilder(this,
                DevCommsDatabase::class.java,
                BuildConfig.roomDbName)
                .allowMainThreadQueries()
                .build()
    }
}