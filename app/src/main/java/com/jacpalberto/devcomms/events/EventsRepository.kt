package com.jacpalberto.devcomms.events

import com.google.firebase.firestore.FirebaseFirestore
import com.jacpalberto.devcomms.BuildConfig
import com.jacpalberto.devcomms.data.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val event = BuildConfig.dbEventName
    private val agendaRef = db.collection("events").document(event).collection("agenda")

    fun fetchEvents(onResult: (events: DataResponse<List<DevCommsEvent>>) -> Unit) {
        checkConnectivity(isConnected = { fetchFirestoreEvents(onResult) },
                isNotConnected = { onResult(DataResponse(emptyList(), DataState.FAILURE)) })
    }

    private fun checkConnectivity(isConnected: () -> Unit, isNotConnected: () -> Unit) {
        db.enableNetwork().addOnCompleteListener { task ->
            if (task.isSuccessful) isConnected()
            else isNotConnected()
        }.addOnFailureListener { isNotConnected() }
    }

    private fun fetchFirestoreEvents(onResult: (events: DataResponse<List<DevCommsEvent>>) -> Unit) {
        val events = mutableListOf<DevCommsEvent>()
        val finalResponse = DataResponse<List<DevCommsEvent>>(emptyList())

        agendaRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val eventListResponse = task.result
                eventListResponse?.forEach { document ->
                    val agenda = document.toObject(AgendaResponse::class.java)
                    agenda.key = document.id

                    agenda.speaker?.get()?.addOnCompleteListener { speakerResponse ->
                        if (speakerResponse.isSuccessful) {
                            val speaker = speakerResponse.result?.toObject(SpeakerDetail::class.java)
                            agenda.speakerDetail = speaker
                            events.add(parseAgendaToDevCommsEvent(agenda))
                            if (events.size == eventListResponse.size()) {
                                onResult(finalResponse.apply { updateSuccessValue(events) })
                            }
                        }
                    }
                }
            } else {
                onResult(finalResponse.apply { setFailureStatus() })
            }
        }.addOnFailureListener { onResult(finalResponse.apply { setFailureStatus() }) }
    }

    private fun parseAgendaToDevCommsEvent(agenda: AgendaResponse): DevCommsEvent {
        with(agenda) {
            val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val startDate = dateFormat.format(agenda.time_start)
            val startTime = timeFormat.format(agenda.time_start)
            val endDate = dateFormat.format(agenda.time_end)
            val endTime = timeFormat.format(agenda.time_end)

            return DevCommsEvent(key = key, title = title, description = description, type = type,
                    time_end = time_end, time_start = time_start, speakerDetail = speakerDetail, room = room,
                    startDateString = startDate, startTimeString = startTime, endDateString = endDate, endTimeString = endTime)
        }
    }
}