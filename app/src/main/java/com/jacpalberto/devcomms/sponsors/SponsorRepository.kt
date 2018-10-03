package com.jacpalberto.devcomms.sponsors

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.jacpalberto.devcomms.BuildConfig
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.MainEventResponse
import com.jacpalberto.devcomms.data.Sponsor

/**
 * Created by Alberto Carrillo on 9/15/18.
 */
class SponsorRepository {
    private val db = FirebaseFirestore.getInstance()
    private val event = BuildConfig.dbEventName
    private val eventRef = db.collection("events").document(event)
    private val database = FirebaseDatabase.getInstance()
    private val connectedRef = database.getReference(".info/connected")

    //TODO: ADD TIMEOUT TO FIRESTORE
    fun fetchSponsors(onResult: (response: DataResponse<List<Sponsor>>) -> Unit) {
        checkConnectivity(isConnected = { fetchFirestoreSponsors(onResult) },
                isNotConnected = { fetchFirestoreSponsors(onResult) })
    }

    private fun checkConnectivity(isConnected: () -> Unit, isNotConnected: () -> Unit) {
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: true
                if (connected) isConnected()
                else isNotConnected()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchFirestoreSponsors(onResult: (response: DataResponse<List<Sponsor>>) -> Unit) {
        val finalResponse = DataResponse<List<Sponsor>>(emptyList(), DataState.SUCCESS)
        val sponsorList = mutableSetOf<Sponsor>()

        eventRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val eventResponse = task.result.toObject(MainEventResponse::class.java)
                val sponsors = eventResponse?.sponsors

                sponsors?.forEach { reference ->
                    reference.id?.get()?.addOnCompleteListener { sponsorResponse ->
                        if (sponsorResponse.isSuccessful) {
                            val sponsor = sponsorResponse.result.toObject(Sponsor::class.java)
                            if (sponsor != null) {
                                sponsor.category = reference.category
                                sponsor.categoryPriority = calculatePriority(reference.category)
                                sponsorList.add(sponsor)
                            }
                            if (sponsorList.size - 1 == sponsors.size - 1) {
                                finalResponse.updateSuccessValue(sponsorList.toList())
                                onResult(finalResponse)
                            }
                        }
                    }
                }
            } else {
                finalResponse.setFailureStatus()
                onResult(finalResponse)
            }
        }
    }

    private fun calculatePriority(category: String) = when (category.toLowerCase()) {
        "platinum" -> 1
        "gold" -> 2
        "silver" -> 3
        else -> 99
    }
}