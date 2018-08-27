package com.jacpalberto.devcomms

import android.app.Application
import android.content.Context

/**
 * Created by Alberto Carrillo on 8/24/18.
 */
class DevCommsApp : Application() {
    companion object {
        private var instance: DevCommsApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }
}