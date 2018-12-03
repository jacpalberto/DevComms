package com.jacpalberto.devcomms.address

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.jacpalberto.devcomms.BuildConfig
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.MainEventResponse
import com.jacpalberto.devcomms.sponsors.models.Location

/**
 * Created by Alberto Carrillo on 12/2/18.
 */
class AddressModel(private val firestore: FirebaseFirestore) {
    private val event = BuildConfig.dbEventName
    private val eventRef = firestore.collection("events").document(event)

    fun fetchLocations(addressLiveData: MutableLiveData<DataResponse<List<Location>>>?) {
        val finalResponse = DataResponse<List<Location>>(emptyList())
        checkConnectivity(isConnected = { fetchFirestoreLocations(addressLiveData) },
                isNotConnected = { addressLiveData?.postValue(finalResponse.apply { setFailureStatus() }) })
    }

    private fun fetchFirestoreLocations(addressLiveData: MutableLiveData<DataResponse<List<Location>>>?) {
        val finalResponse = DataResponse<List<Location>>(emptyList())

        eventRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val eventResponse = task.result?.toObject(MainEventResponse::class.java)
                val locations = eventResponse?.locations ?: emptyList()
                addressLiveData?.postValue(finalResponse.apply { updateSuccessValue(locations) })
            } else {
                addressLiveData?.postValue(finalResponse.apply { setFailureStatus() })
            }
        }
    }

    private fun checkConnectivity(isConnected: () -> Unit, isNotConnected: () -> Unit) {
        firestore.enableNetwork().addOnCompleteListener { task ->
            if (task.isSuccessful) isConnected()
            else isNotConnected()
        }
    }
}