package com.jacpalberto.devcomms.events

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DevCommsEventList

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventsViewModel : ViewModel() {
    private var events: MutableLiveData<DevCommsEventList>? = null

    fun fetchEvents(): MutableLiveData<DevCommsEventList>? {
        if (events == null) {
            events = MutableLiveData()
            EventsModel.fetchEvents { events?.postValue(it) }
        }
        return events
    }
}


