package com.jacpalberto.devcomms.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
object FirebaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val eventsRef = database.getReference("posadev")
    private val sponsorsRef = database.getReference("spnosors")
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

    fun fetchSponsors(onResult: (sponsors: SponsorList) -> Unit) {
        checkConnectivity(isConnected = { fetchFirebaseSponsors(onResult) },
                isNotConnected = { onResult(SponsorList(emptyList(), 400, DataState.ERROR)) })
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

    private fun fetchFirebaseSponsors(onResult: (sponsors: SponsorList) -> Unit) {
        var status: DataState
        var sponsorList: List<Sponsor> = emptyList()
        var errorCode = 0

        sponsorsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sponsors = dataSnapshot.children.map { it.getValue(Sponsor::class.java) }
                sponsors.forEach { if (it != null) sponsorList += it }
                status = DataState.SUCCESS
                onResult(SponsorList(sponsorList, errorCode, status))
            }

            override fun onCancelled(error: DatabaseError) {
                status = DataState.FAILURE
                errorCode = error.code
                onResult(SponsorList(sponsorList, errorCode, status))
            }
        })
    }
}