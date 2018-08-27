package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import com.jacpalberto.devcomms.DevCommsApp
import com.jacpalberto.devcomms.DevCommsDatabase
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.Repository
import com.jacpalberto.devcomms.data.Sponsor

/**
 * Created by Alberto Carrillo on 8/20/18.
 */
object SponsorsModel {
    private val db by lazy { DevCommsDatabase.getInstance(DevCommsApp.applicationContext()) }
    //private val sponsorsDao by lazy { db.sponsorsDao() }
    private var sponsors: LiveData<List<Sponsor?>>? = null//= sponsorsDao.getAll()

    fun getSponsors(): LiveData<List<Sponsor?>>? {
        val sponsorResponse = Repository.fetchSponsors()
        Log.d(this::class.java.name, sponsorResponse.toString())
        if (sponsorResponse.status == DataState.FAILURE || sponsorResponse.status == DataState.ERROR) {
            //val sponsorTemp = sponsorsDao.getAll()
            //if (sponsorTemp.value == null)
            return sponsors
        } else saveAll(sponsorResponse.eventList)
        return sponsors
    }

    private fun saveAll(sponsors: List<Sponsor>) {
        //InsertAsyncTask(sponsorsDao).execute(sponsors)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: SponsorsDao) :
            AsyncTask<List<Sponsor>, Void, Void>() {
        override fun doInBackground(vararg params: List<Sponsor>): Void? {
            mAsyncTaskDao.save(params[0])
            return null
        }
    }
}