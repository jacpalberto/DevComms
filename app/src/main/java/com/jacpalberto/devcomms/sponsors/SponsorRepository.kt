package com.jacpalberto.devcomms.sponsors

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.data.SponsorList

/**
 * Created by Alberto Carrillo on 9/15/18.
 */
//TODO: replace JavaDevDay from string to BuildConfig variable
class SponsorRepository {
    private var db = FirebaseFirestore.getInstance()
    private var appName = "JavaDevDay"
    private val sponsorsRef = db.collection("App/$appName/sponsors")

    fun fetchSponsors(onResult: (sponsors: SponsorList) -> Unit) {
        fetchFirestoreSponsors(onResult)
    }

    private fun fetchFirestoreSponsors(onResult: (sponsors: SponsorList) -> Unit) {
        var sponsorList: List<Sponsor> = emptyList()
        sponsorsRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                it.result.forEach { sponsor -> sponsorList += sponsor.toObject(Sponsor::class.java) }
                onResult(SponsorList(sponsorList, 0, DataState.SUCCESS))
            } else onFailure(onResult, sponsorList)
        }.addOnFailureListener {
            onFailure(onResult, sponsorList)
        }.addOnCanceledListener {
            onFailure(onResult, sponsorList)
        }
    }

    private fun onFailure(onResult: (sponsors: SponsorList) -> Unit, sponsorList: List<Sponsor>) {
        onResult(SponsorList(sponsorList, 400, DataState.FAILURE))
    }
}