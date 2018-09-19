package com.jacpalberto.devcomms.data

import android.arch.persistence.room.*
import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.gson.annotations.SerializedName
import com.jacpalberto.devcomms.sponsors.models.Location
import com.jacpalberto.devcomms.sponsors.models.SponsorResponse
import kotlinx.android.parcel.Parcelize
import java.time.OffsetDateTime
import java.util.*

/**
 * Created by Alberto Carrillo on 7/12/18.
 */
@Parcelize
@Entity(tableName = "events")
data class DevCommsEvent(@PrimaryKey val key: String = "",
                         val title: String? = "",
                         val type: String? = "",
                         val description: String? = "",
                         val time_start: Date? = null,
                         val startDateString: String? = "",
                         val startTimeString: String? = "",
                         val time_end: Date? = null,
                         val endDateString: String? = "",
                         val endTimeString: String? = "",
                         @Embedded var speakerDetail: SpeakerDetail? = SpeakerDetail(),
                         var isFavorite: Boolean? = false,
                         val room: String? = "") : Parcelable {
    @Ignore constructor() : this("")
}

@Parcelize
data class SpeakerDetail(val first_name: String? = "",
                         val last_name: String? = "",
                         val bio: String? = "",
                         val country: String? = "",
                         val photo_url: String? = "",
                         val twitter: String? = "",
                         val github: String? = "") : Parcelable {
    @Ignore constructor() : this("")
}

@Entity(tableName = "sponsors")
data class Sponsor(@PrimaryKey(autoGenerate = true) val key: Int = 0,
                   val logo_url: String? = "",
                   val contact: String? = "",
                   val brief: String? = "",
                   val web: String? = "",
                   val name: String? = "") {

    @Ignore constructor() : this(0)
}

data class AgendaResponse(
        var key: String = "",
        val description: String = "",
        val room: String = "",
        val time_end: Date = Date(),
        val time_start: Date = Date(),
        val title: String = "",
        val type: String = "",
        var speakerDetail: SpeakerDetail? = SpeakerDetail(),
        val speaker: DocumentReference?
) {
    constructor() : this(speaker = null)
}

data class MainEventResponse(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("locations")
        val locations: List<Location> = emptyList(),
        @SerializedName("communities")
        val communities: List<DocumentReference> = emptyList(),
        @SerializedName("dates")
        val dates: List<Date> = emptyList(),
        @SerializedName("description")
        val description: String = "",
        @SerializedName("sponsors")
        val sponsors: List<SponsorResponse> = mutableListOf()
)

@Parcelize
data class DevCommsEventList(val eventList: List<DevCommsEvent>,
                             override var errorCode: Int = 0,
                             override var status: DataState = DataState.PROCESS) : Parcelable, BaseModel(errorCode, status)

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