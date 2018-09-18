package com.jacpalberto.devcomms.sponsors

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.data.SponsorList

/**
 * Created by Alberto Carrillo on 9/15/18.
 */
//TODO: replace JavaDevDay from string to BuildConfig variable
class SponsorRepository {
    private val db = FirebaseFirestore.getInstance()
    val event = "jdd-18"
    val eventRef = db.collection("events").document(event)
    var sponsorList = emptyList<Sponsor>()
    private val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")

    fun fetchSponsors(onResult: (sponsors: SponsorList) -> Unit) {
        checkConnectivity(isConnected = { fetchFirestoreSponsors(onResult) },
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

    private fun fetchFirestoreSponsors(onResult: (sponsors: SponsorList) -> Unit) {
        var sponsorList: List<Sponsor> = emptyList()
        eventRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("MainActivity2", it.result.data.toString())
            }
        }
       //sponsorsRef.get().addOnCompleteListener {
       //    if (it.isSuccessful) {
       //        it.result.forEach { sponsor -> sponsorList += sponsor.toObject(Sponsor::class.java) }
       //        onResult(SponsorList(sponsorList, 0, DataState.SUCCESS))
       //    } else onFailure(onResult, sponsorList)
       //}.addOnFailureListener {
       //    onFailure(onResult, sponsorList)
       //}
    }

    private fun onFailure(onResult: (sponsors: SponsorList) -> Unit, sponsorList: List<Sponsor>) {
        onResult(SponsorList(sponsorList, 400, DataState.FAILURE))
    }
}