package com.jacpalberto.devcomms.events

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.jacpalberto.devcomms.data.*

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventsRepository {
    private val database = FirebaseDatabase.getInstance()
    private val connectedRef = database.getReference(".info/connected")
    private val db = FirebaseFirestore.getInstance()
    private val event = "jdd-18"
    private val agendaRef = db.collection("events").document(event).collection("agenda")
    private val eventsList = mutableListOf<DevCommsEvent>()

    fun fetchEvents(onResult: (events: DevCommsEventList) -> Unit) {
        checkConnectivity(isConnected = { fetchFirestoreEvents(onResult) },
                isNotConnected = { onResult(DevCommsEventList(emptyList(), 400, DataState.ERROR)) })
    }

    private fun fetchFirestoreEvents(onResult: (events: DevCommsEventList) -> Unit) {
        agendaRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val eventListResponse = it.result
                eventListResponse.forEachIndexed { index, document ->
                    val agenda = document.toObject(AgendaResponse::class.java)
                    agenda.key = document.id

                    agenda.speaker?.get()?.addOnCompleteListener { speakerResponse ->
                        if (speakerResponse.isSuccessful) {
                            val speaker = speakerResponse.result.toObject(SpeakerDetail::class.java)
                            agenda.speakerDetail = speaker
                            eventsList.add(parseAgendaToDevCommsEvent(agenda))
                            if (index == eventsList.size - 1) {
                                onResult(DevCommsEventList(eventsList, 0, DataState.SUCCESS))
                                Log.d("EventsRepo",eventsList.toString())
                            }
                        }
                    }
                }
            } else {
                onResult(DevCommsEventList(emptyList(), 400, DataState.ERROR))
            }
        }
    }

    private fun parseAgendaToDevCommsEvent(agenda: AgendaResponse): DevCommsEvent {
        with(agenda) {
            return DevCommsEvent(key = key, title = title, description = description, type = type,
                    time_end = time_end, time_start = time_start, speakerDetail = speakerDetail, room = room)
        }
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