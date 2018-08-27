package com.jacpalberto.devcomms.events

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.Repository

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventsViewModel : ViewModel() {
    private var events: MutableLiveData<List<DevCommsEvent?>>? = null

    fun fetchEvents(): MutableLiveData<List<DevCommsEvent?>> {
        if (events == null) {
            events = MutableLiveData()
            Repository.fetchEvents(onSuccess = { events?.value = it }, onError = { })
        }
        return events as MutableLiveData<List<DevCommsEvent?>>
    }
}


