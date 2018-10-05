package com.jacpalberto.devcomms.events

import android.arch.lifecycle.MutableLiveData
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

    fun fetchEvents(liveData: MutableLiveData<DataResponse<List<DevCommsEvent>>>) {
        var events: List<DevCommsEvent>
        val finalResponse = DataResponse<List<DevCommsEvent>>(emptyList(), DataState.SUCCESS)

        repository.fetchEvents { response ->
            if (response.isStatusFailedOrError()) {
                events = eventsDao.getList()
                if (events.isEmpty()) liveData.postValue(finalResponse.apply { setFailureStatus() })
                else liveData.postValue (finalResponse.apply { updateSuccessValue(events) })
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
                    liveData.postValue(finalResponse.apply { updateSuccessValue(events) })
                }
            }
        }
    }

    fun fetchFavoriteEvents(liveData: MutableLiveData<DataResponse<List<DevCommsEvent>>>) {
        AsyncTask.execute {
            val favoriteList = eventsDao.getFavoriteList()
            liveData.postValue(DataResponse(favoriteList, DataState.SUCCESS))
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
