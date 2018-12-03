package com.jacpalberto.devcomms.address

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.sponsors.models.Location

/**
 * Created by Alberto Carrillo on 12/2/18.
 */
class AddressViewModel(private val model: AddressModel) : ViewModel() {
    private var addressLiveData: MutableLiveData<DataResponse<List<Location>>>? = null

    fun fetchLocations(): MutableLiveData<DataResponse<List<Location>>>? {
        if (addressLiveData == null) {
            addressLiveData = MutableLiveData()
        }
        model.fetchLocations(addressLiveData)
        return addressLiveData
    }
}
