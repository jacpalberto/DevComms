package com.jacpalberto.devcomms.sponsors.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.parcel.Parcelize

/**
 * Created by Alberto Carrillo on 9/18/18.
 */
data class SponsorResponse(
        val id: DocumentReference?,
        val category: String = ""
) {
    constructor() : this(null, "")
}

@Parcelize
data class Location(
        val name: String = "",
        val lon: Float = 0.0F,
        val lat: Float = 0.0F
) : Parcelable