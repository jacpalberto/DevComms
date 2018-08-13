package com.jacpalberto.devcomms.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Alberto Carrillo on 7/12/18.
 */

@Parcelize
data class DevCommsEvent(val key: String? = "",
                         val hour: String? = "",
                         val title: String? = "",
                         val speaker: String? = "",
                         val speakerPhotoUrl: String? = "",
                         val community: String? = "",
                         val type: String? = "",
                         val room: String? = "") : Parcelable

@Parcelize
data class DevCommsListEvent(val eventList: List<DevCommsEvent?>) : Parcelable, BaseModel()

open class BaseModel {
    var errorCode = 0
    var status: DataState = DataState.NONE
}

enum class DataState {
    NONE,
    SUCCESS,
    PROCESS,
    ERROR,
    FAILURE
}