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
@Entity(tableName = "events")
data class DevCommsEvent(@PrimaryKey val key: Int? = 0,
                         val hour: String? = "",
                         val date: String? = "",
                         val title: String? = "",
                         val type: String? = "",
                         val description: String? = "",
                         val community: String? = "",

                         val speaker: String? = "",
                         val speakerDescription: String? = "",
                         val speakerPhotoUrl: String? = "",
                         val company: String? = "",
                         val githubUrl: String? = "",
                         val webPageUrl: String? = "",

                         var isFavorite: Boolean? = false,
                         val room: String? = "") : Parcelable {
    @Ignore constructor() : this(0)
}

@Parcelize
data class SpeakerDetail(val first_name: String? = "",
                         val last_name: String? = "",
                         val bio: String? = "",
                         val country: String? = "",
                         val photo_url: String? = "",
                         val twitter: String? = "",
                         val github: String? = "",
                         val email: String? = "") : Parcelable

@Entity(tableName = "sponsors")
data class Sponsor(@PrimaryKey val key: Int = 0,
                   val logo_url: String? = "",
                   val contact: String? = "",
                   val brief: String? = "",
                   val web: String? = "",
                   val name: String? = "") {

    @Ignore constructor() : this(0)
}

@Parcelize
data class DevCommsEventList(val eventList: List<DevCommsEvent>,
                             override var errorCode: Int = 0,
                             override var status: DataState = DataState.PROCESS)
    : Parcelable, BaseModel(errorCode, status)

data class SponsorList(val sponsorList: List<Sponsor> = emptyList(),
                       override var errorCode: Int = 0,
                       override var status: DataState = DataState.PROCESS)
    : BaseModel(errorCode, status)

open class BaseModel(open var errorCode: Int = 0,
                     open var status: DataState = DataState.NONE)

enum class DataState {
    NONE,
    SUCCESS,
    PROCESS,
    ERROR,
    FAILURE
}