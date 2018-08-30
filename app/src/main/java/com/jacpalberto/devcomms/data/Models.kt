package com.jacpalberto.devcomms.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Alberto Carrillo on 7/12/18.
 */

@Parcelize
data class DevCommsEvent(val key: String? = "",
                         val hour: String? = "",
                         val date: String? = "",
                         val title: String? = "",
                         val speaker: String? = "",
                         val speakerPhotoUrl: String? = "",
                         val community: String? = "",
                         val type: String? = "",
                         val room: String? = "") : Parcelable

@Parcelize
data class DevCommsListEvent(val eventList: List<DevCommsEvent?>) : Parcelable

@Entity(tableName = "sponsors")
data class Sponsor(@PrimaryKey val key: Int = 0,
                   val imageUrl: String? = "",
                   val webPageUrl: String? = "",
                   val title: String? = "") {
    @Ignore constructor() : this(0, "", "", "")
}

data class SponsorList(val sponsorList: List<Sponsor> = emptyList(),
                       override var errorCode: Int = 0,
                       override var status: DataState = DataState.PROCESS) : BaseModel(errorCode, status)

open class BaseModel(open var errorCode: Int = 0,
                     open var status: DataState = DataState.NONE)

enum class DataState {
    NONE,
    SUCCESS,
    PROCESS,
    ERROR,
    FAILURE
}