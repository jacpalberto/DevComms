package com.jacpalberto.devcomms.sponsors.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Alberto Carrillo on 9/18/18.
 */
data class SponsorResponse(
        @SerializedName("id")
        val id: DocumentReference?,
        @SerializedName("category")
        val category: String = ""
) {
    constructor() : this(null, "")
}

@Parcelize
data class Location(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("lon")
        val lon: Float = 0.0F,
        @SerializedName("lat")
        val lat: Float = 0.0F
) : Parcelable