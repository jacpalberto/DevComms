package com.jacpalberto.devcomms.events

import android.os.AsyncTask
import com.jacpalberto.devcomms.DevCommsApp
import com.jacpalberto.devcomms.data.*

/**
 * Created by Alberto Carrillo on 8/30/18.
 */
class EventsModel {
    private val db by lazy { DevCommsApp.database }
    private val eventsDao by lazy { db!!.eventsDao() }
    private var eventList: List<DevCommsEvent> = emptyList()
    private val repository = EventsRepository()

    fun fetchEvents(onResult: (DevCommsEventList) -> Unit) {
        lateinit var eventsResult: DevCommsEventList
        repository.fetchEvents { response ->
            if (response.status == DataState.FAILURE || response.status == DataState.ERROR) {
                eventList = eventsDao.getList()
                eventsResult = if (eventList.isEmpty())
                    DevCommsEventList(eventList, status = DataState.FAILURE)
                else DevCommsEventList(eventList, status = DataState.SUCCESS)
                onResult(eventsResult)
            } else {
                val favoriteList = eventsDao.getFavoriteList()
                if (favoriteList.isEmpty()) {
                    saveAll(response.eventList)
                } else {
                    updateFavoriteFields(favoriteList, response)
                    saveAll(response.eventList)
                }
                AsyncTask.execute {
                    eventList = eventsDao.getList()
                    eventsResult = DevCommsEventList(eventList, status = DataState.SUCCESS)
                    onResult(eventsResult)
                }
            }
        }
    }

    fun fetchFavoriteEvents(onResult: (DevCommsEventList) -> Unit ) {
        AsyncTask.execute {
            val favoriteList = eventsDao.getFavoriteList()
            val eventsResult = DevCommsEventList(favoriteList, status = DataState.SUCCESS)
            onResult(eventsResult)
        }
    }

    fun toggleFavorite(key: Int, isFavorite: Boolean) {
        eventsDao.updateFavorite(key, isFavorite)
    }

    private fun updateFavoriteFields(favoriteList: List<DevCommsEvent>, response: DevCommsEventList) {
        for (item in favoriteList) {
            for (res in response.eventList) {
                if (res.key == item.key) {
                    res.isFavorite = true
                    break
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
