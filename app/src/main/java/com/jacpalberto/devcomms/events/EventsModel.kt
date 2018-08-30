package com.jacpalberto.devcomms.events

import android.os.AsyncTask
import com.jacpalberto.devcomms.DevCommsApp
import com.jacpalberto.devcomms.data.*

/**
 * Created by Alberto Carrillo on 8/30/18.
 */
object EventsModel {
    private val db by lazy { DevCommsApp.database }
    private val eventsDao by lazy { db!!.eventsDao() }
    private var sponsorsList: List<DevCommsEvent> = emptyList()

    fun fetchEvents(onResult: (DevCommsEventList) -> Unit) {
        lateinit var sponsorResult: DevCommsEventList
        FirebaseRepository.fetchEvents { response ->
            if (response.status == DataState.FAILURE || response.status == DataState.ERROR) {
                //sponsorsList = sponsorsDao.getList()
                sponsorResult = if (sponsorsList.isEmpty())
                    DevCommsEventList(sponsorsList, status = DataState.FAILURE)
                else DevCommsEventList(sponsorsList, status = DataState.SUCCESS)
                onResult(sponsorResult)
            } else {
                //saveAll(response.sponsorList)
                //AsyncTask.execute {
                //sponsorsList = sponsorsDao.getList()
                //TODO: change response for sponsorList
                sponsorResult = DevCommsEventList(response.eventList, status = DataState.SUCCESS)
                onResult(sponsorResult)
            }
        }
    }
}

/*private fun saveAll(sponsors: List<Sponsor>) {
     AsyncTask.execute {
         sponsorsDao.deleteAll()
         sponsorsDao.save(sponsors) }
 }*/
