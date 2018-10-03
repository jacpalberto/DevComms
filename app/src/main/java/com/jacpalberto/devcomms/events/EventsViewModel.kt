package com.jacpalberto.devcomms.events

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.DevCommsEvent

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventsViewModel : ViewModel() {
    var fragmentTag: String = EventsByDateFragment.TAG
    private var model = EventsModel()
    private var events: MutableLiveData<DataResponse<List<DevCommsEvent>>>? = null
    private var favoriteEvents: MutableLiveData<DataResponse<List<DevCommsEvent>>>? = null

    fun fetchEvents(): MutableLiveData<DataResponse<List<DevCommsEvent>>>? {
        if (events == null) {
            events = MutableLiveData()
        }
        model.fetchEvents { events?.postValue(it) }
        return events
    }

    fun fetchFavoriteEvents(): MutableLiveData<DataResponse<List<DevCommsEvent>>>? {
        if (favoriteEvents == null) {
            favoriteEvents = MutableLiveData()
        }
        model.fetchFavoriteEvents { favoriteEvents?.postValue(it) }
        return favoriteEvents
    }

    fun updateAgenda() {
        model.fetchFavoriteEvents { favoriteEvents?.postValue(it) }
    }

    fun toggleFavorite(key: String, isFavorite: Boolean) {
        model.toggleFavorite(key, isFavorite)
    }
}


