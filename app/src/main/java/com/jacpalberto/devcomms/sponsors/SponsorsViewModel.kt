package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jacpalberto.devcomms.data.Sponsor

/**
 * Created by Alberto Carrillo on 8/13/18.
 */
class SponsorsViewModel : ViewModel() {
    private var sponsors: LiveData<List<Sponsor?>>? = null

    fun fetchSponsors(): LiveData<List<Sponsor?>>? {
        if (sponsors == null){
            sponsors = MutableLiveData()
            sponsors = SponsorsModel.getSponsors()
        }
        return sponsors
    }
}
