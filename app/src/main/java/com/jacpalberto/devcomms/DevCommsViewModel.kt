package com.jacpalberto.devcomms

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.google.firebase.database.DatabaseError

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class DevCommsViewModel : ViewModel() {
    private var events: MutableLiveData<List<DevCommsEvent?>>? = null
    private var error: MutableLiveData<Unit>? = null
    private var isFilteredByTime: Boolean = true

    fun getEvents(): LiveData<List<DevCommsEvent?>>? {
        if (events == null) {
            events = MutableLiveData()
            Repository.fetchEvents(onSuccess = { events?.value = it }, onError = { showError(it) })
        }

        return events
    }

    fun getFilteredByTime() = isFilteredByTime

    fun setFilteredByTime(isFilteredByTime: Boolean) {
        this.isFilteredByTime = isFilteredByTime
    }

    private fun showError(error: DatabaseError) {
        Log.d("vm", "Error ${error.message}")
    }
}


