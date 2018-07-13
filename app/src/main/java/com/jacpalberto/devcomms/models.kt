package com.jacpalberto.devcomms

/**
 * Created by Alberto Carrillo on 7/12/18.
 */
data class DevCommsEvent(val key: String? = "",
                         val hour: String? = "",
                         val title: String? = "",
                         val speaker: String? = "",
                         val speakerPhotoUrl: String? = "",
                         val community: String? = "",
                         val type: String? = "",
                         val room: String? = "")