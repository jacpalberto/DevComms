package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.Sponsor

/**
 * Created by Alberto Carrillo on 8/13/18.
 */
class SponsorsViewModel : ViewModel() {
    private var sponsorsLiveData: MutableLiveData<DataResponse<List<Sponsor>>>? = null
    private val model = SponsorsModel()

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
}
