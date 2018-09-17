package com.jacpalberto.devcomms.events

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.DevCommsEventList

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventsRepository {
    private val database = FirebaseDatabase.getInstance()
    private val eventsRef = database.getReference("posadev")
    private val connectedRef = database.getReference(".info/connected")

    fun fetchEvents(onResult: (events: DevCommsEventList) -> Unit) {
        checkConnectivity(isConnected = { fetchFirebaseEvents(onResult) },
                isNotConnected = { onResult(DevCommsEventList(emptyList(), 400, DataState.ERROR)) })
    }

    private fun fetchFirebaseEvents(onResult: (events: DevCommsEventList) -> Unit) {
        var status: DataState
        var eventList: List<DevCommsEvent> = emptyList()
        var errorCode = 0

        eventsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val events = dataSnapshot.children.map { it.getValue(DevCommsEvent::class.java) }
                events.forEach { if (it != null) eventList += it }
                status = DataState.SUCCESS
                onResult(DevCommsEventList(eventList, errorCode, status))
            }

            override fun onCancelled(error: DatabaseError) {
                status = DataState.FAILURE
                errorCode = error.code
                onResult(DevCommsEventList(eventList, errorCode, status))
            }
        })
    }

    private fun checkConnectivity(isConnected: () -> Unit, isNotConnected: () -> Unit) {
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) isConnected()
                else isNotConnected()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}