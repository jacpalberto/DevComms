package com.jacpalberto.devcomms.events

import android.os.AsyncTask
import com.jacpalberto.devcomms.DevCommsApp
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.DevCommsEvent

/**
 * Created by Alberto Carrillo on 8/30/18.
 */
class EventsModel {
    private val db by lazy { DevCommsApp.database }
    private val eventsDao by lazy { db!!.eventsDao() }
    private val repository = EventsRepository()

    fun fetchEvents(onResult: (events: DataResponse<List<DevCommsEvent>>) -> Unit) {
        var events: List<DevCommsEvent>
        val finalResponse = DataResponse<List<DevCommsEvent>>(emptyList(), DataState.SUCCESS)

        repository.fetchEvents { response ->
            if (response.isStatusFailedOrError()) {
                events = eventsDao.getList()
                if (events.isEmpty()) onResult(finalResponse.apply { setFailureStatus() })
                else onResult(finalResponse.apply { updateSuccessValue(events) })
            } else {
                val favoriteList = eventsDao.getFavoriteList()
                if (favoriteList.isEmpty()) {
                    saveAll(response.data)
                } else {
                    updateFavoriteFields(favoriteList, response)
                    saveAll(response.data)
                }
                AsyncTask.execute {
                    events = eventsDao.getList()
                    onResult(finalResponse.apply { updateSuccessValue(events) })
                }
            }
        }
    }

    fun fetchFavoriteEvents(onResult: (events: DataResponse<List<DevCommsEvent>>) -> Unit) {
        AsyncTask.execute {
            val favoriteList = eventsDao.getFavoriteList()
            onResult(DataResponse(favoriteList, DataState.SUCCESS))
        }
    }

    fun toggleFavorite(key: String, isFavorite: Boolean) {
        eventsDao.updateFavorite(key, isFavorite)
    }

    private fun updateFavoriteFields(favoriteList: List<DevCommsEvent>, response: DataResponse<List<DevCommsEvent>>) {
        for (item in favoriteList) {
            for (res in response.data) {
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
