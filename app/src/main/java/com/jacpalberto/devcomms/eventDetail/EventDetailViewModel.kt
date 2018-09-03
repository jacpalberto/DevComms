package com.jacpalberto.devcomms.eventDetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DevCommsEvent

/**
 * Created by Alberto Carrillo on 8/30/18.
 */
class EventDetailViewModel : ViewModel() {
    var devCommsEvent: MutableLiveData<DevCommsEvent>? = null

    fun setEvent(event: DevCommsEvent) {
        if (devCommsEvent == null) {
            devCommsEvent = MutableLiveData()
        }
        devCommsEvent?.value = event
    }

    fun getEvent(): MutableLiveData<DevCommsEvent>? {
        if (devCommsEvent == null) {
            devCommsEvent = MutableLiveData()
        }
        return devCommsEvent
    }
}