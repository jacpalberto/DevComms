package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.Sponsor

/**
 * Created by Alberto Carrillo on 8/13/18.
 */
class SponsorsViewModel : ViewModel() {
    private var sponsors: MutableLiveData<DataResponse<List<Sponsor>>>? = null
    private val model = SponsorsModel()

    fun fetchSponsors(): MutableLiveData<DataResponse<List<Sponsor>>>? {
        if (sponsors == null) {
            sponsors = MutableLiveData()
            model.fetchSponsors { sponsors?.postValue(it) }
        }
        return sponsors
    }

    fun refreshSponsors() {
        model.fetchSponsors { sponsors?.postValue(it) }
    }
}
