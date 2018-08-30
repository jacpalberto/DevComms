package com.jacpalberto.devcomms.events

import android.os.AsyncTask
import android.util.Log
import com.jacpalberto.devcomms.DevCommsApp
import com.jacpalberto.devcomms.data.*

/**
 * Created by Alberto Carrillo on 8/30/18.
 */
object EventsModel {
    private val db by lazy { DevCommsApp.database }
    private val eventsDao by lazy { db!!.eventsDao() }
    private var eventList: List<DevCommsEvent> = emptyList()

    fun fetchEvents(onResult: (DevCommsEventList) -> Unit) {
        lateinit var eventsResult: DevCommsEventList
        FirebaseRepository.fetchEvents { response ->
            Log.d("EventsModel", response.toString())
            if (response.status == DataState.FAILURE || response.status == DataState.ERROR) {
                eventList = eventsDao.getList()
                eventsResult = if (eventList.isEmpty())
                    DevCommsEventList(eventList, status = DataState.FAILURE)
                else DevCommsEventList(eventList, status = DataState.SUCCESS)
                Log.d("EventsModelfail", eventsResult.toString())
                onResult(eventsResult)
            } else {
                saveAll(response.eventList)
                AsyncTask.execute {
                    eventList = eventsDao.getList()
                    eventsResult = DevCommsEventList(eventList, status = DataState.SUCCESS)
                    Log.d("EventsModelsucc", eventsResult.toString())
                    onResult(eventsResult)
                }
            }
        }
    }

    private fun saveAll(events: List<DevCommsEvent>) {
        AsyncTask.execute {
            eventsDao.deleteAll()
            eventsDao.save(events)
        }
    }
}
