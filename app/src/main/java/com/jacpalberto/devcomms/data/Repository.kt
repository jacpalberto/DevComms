package com.jacpalberto.devcomms.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
object Repository {
    private val database = FirebaseDatabase.getInstance()
    private val eventsRef = database.getReference("posadev")
    private val sponsorsRef = database.getReference("sponsors")

    fun fetchEvents(onSuccess: (events: List<DevCommsEvent?>) -> Unit, onError: (error: DatabaseError) -> Unit) {
        eventsRef.orderByChild("time").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val events = dataSnapshot.children.map { it.getValue(DevCommsEvent::class.java) }
                Log.d("Repository", events.toString())
                onSuccess(events)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    fun fetchSponsors(): SponsorList {
        var sponsorList: List<Sponsor> = emptyList()
        var status = DataState.ERROR
        var errorCode = 0
        sponsorsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sponsors = dataSnapshot.children.map { it.getValue(Sponsor::class.java) }
                Log.d("Repository", sponsors.toString())
                sponsors.forEach {
                    if (it != null) sponsorList += it
                }
                status = DataState.SUCCESS
            }

            override fun onCancelled(error: DatabaseError) {
                status = DataState.FAILURE
                errorCode = error.code
            }
        })
        return SponsorList(sponsorList, errorCode, status)
    }
}