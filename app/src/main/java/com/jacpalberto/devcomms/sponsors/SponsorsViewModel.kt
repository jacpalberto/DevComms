package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.jacpalberto.devcomms.data.SponsorList

/**
 * Created by Alberto Carrillo on 8/13/18.
 */
class SponsorsViewModel : ViewModel() {
    private var sponsors: MutableLiveData<SponsorList>? = null
    private val model = SponsorsModel()

    fun fetchSponsors(): MutableLiveData<SponsorList>? {
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
