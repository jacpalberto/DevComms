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

    fun fetchEvents(onSuccess: (events: List<DevCommsEvent?>) -> Unit, onError: (error: DatabaseError) -> Unit) {
        eventsRef.orderByChild("time").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val events = dataSnapshot.children.map { it.getValue(DevCommsEvent::class.java) }
                onSuccess(events)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    fun fetchSponsors(onResult: (sponsors: SponsorList) -> Unit) {
        val sponsorList: List<Sponsor> = emptyList()
        val errorCode = 0
        checkConnectivity(isConnected = { fetchSponsorsFirebase(sponsorList, onResult, errorCode) },
                isNotConnected = { onResult(SponsorList(sponsorList, 400, DataState.ERROR)) })
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

    private fun fetchSponsorsFirebase(sponsorList: List<Sponsor>, onResult: (sponsors: SponsorList) -> Unit, errorCode: Int) {
        var sponsorList1 = sponsorList
        var status1: DataState
        var errorCode1 = errorCode

        sponsorsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sponsors = dataSnapshot.children.map { it.getValue(Sponsor::class.java) }
                sponsors.forEach { if (it != null) sponsorList1 += it }
                status1 = DataState.SUCCESS
                onResult(SponsorList(sponsorList1, errorCode1, status1))
            }

            override fun onCancelled(error: DatabaseError) {
                status1 = DataState.FAILURE
                errorCode1 = error.code
                onResult(SponsorList(sponsorList1, errorCode1, status1))
            }
        })
    }
}