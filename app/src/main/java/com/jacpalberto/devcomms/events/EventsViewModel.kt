package com.jacpalberto.devcomms.events

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DevCommsEventList

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventsViewModel : ViewModel() {
    var fragmentTag: String = EventsByDateFragment.TAG
    private var model = EventsModel()
    private var events: MutableLiveData<DevCommsEventList>? = null
    private var favoriteEvents: MutableLiveData<DevCommsEventList>? = null

    fun fetchEvents(): MutableLiveData<DevCommsEventList>? {
        if (events == null) {
            events = MutableLiveData()
        }
        model.fetchEvents { events?.postValue(it) }
        return events
    }

    fun fetchFavoriteEvents(): MutableLiveData<DevCommsEventList>? {
        if (favoriteEvents == null) {
            favoriteEvents = MutableLiveData()
        }
        model.fetchFavoriteEvents { favoriteEvents?.postValue(it) }
        return favoriteEvents
    }

    fun toggleFavorite(key: Int, isFavorite: Boolean) {
        model.toggleFavorite(key, isFavorite)
    }
}


