package com.jacpalberto.devcomms.events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.DevCommsEvent

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventsViewModel : ViewModel() {
    var fragmentTag: String = EventsByDateFragment.TAG
    private var model = EventsModel()
    private var eventsLiveData: MutableLiveData<DataResponse<List<DevCommsEvent>>> = MutableLiveData()
    private var favoriteEventsLiveData: MutableLiveData<DataResponse<List<DevCommsEvent>>> = MutableLiveData()

    fun fetchEvents(): MutableLiveData<DataResponse<List<DevCommsEvent>>> {
        model.fetchEvents(eventsLiveData)
        return eventsLiveData
    }

    fun fetchFavoriteEvents(): MutableLiveData<DataResponse<List<DevCommsEvent>>> {
        model.fetchFavoriteEvents(favoriteEventsLiveData)
        return favoriteEventsLiveData
    }

    fun updateAgenda() {
        model.fetchFavoriteEvents(favoriteEventsLiveData)
    }

    fun toggleFavorite(key: String, isFavorite: Boolean) {
        model.toggleFavorite(key, isFavorite)
    }
}


