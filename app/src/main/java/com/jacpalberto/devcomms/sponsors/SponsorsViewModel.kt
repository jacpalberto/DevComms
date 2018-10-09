package com.jacpalberto.devcomms.sponsors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.Sponsor

/**
 * Created by Alberto Carrillo on 8/13/18.
 */
class SponsorsViewModel : ViewModel() {
    //TODO: inject model
    private val model = SponsorsModel()

    private var sponsorsLiveData: MutableLiveData<DataResponse<List<Sponsor>>>? = null

    fun fetchSponsors(): MutableLiveData<DataResponse<List<Sponsor>>>? {
        if (sponsorsLiveData == null) {
            sponsorsLiveData = MutableLiveData()
            model.fetchSponsors(sponsorsLiveData)
        }
        return sponsorsLiveData
    }

    fun refreshSponsors() {
        model.fetchSponsors(sponsorsLiveData)
    }

    fun onDestroy() {
        model.onDestroy()
    }
}
