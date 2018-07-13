package com.jacpalberto.devcomms

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Singleton

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
@Singleton
object Repository {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("posadev")

    fun fetchEvents(onSuccess: (events: List<DevCommsEvent?>) -> Unit, onError: (error: DatabaseError) -> Unit) {
        myRef.orderByChild("time").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val events = dataSnapshot.children.map { it.getValue(DevCommsEvent::class.java) }
                onSuccess(events)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }
}