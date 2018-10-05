package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import com.jacpalberto.devcomms.DevCommsApp
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.Sponsor


/**
 * Created by Alberto Carrillo on 8/20/18.
 */
class SponsorsModel {
    private val db by lazy { DevCommsApp.database }
    private val sponsorsDao by lazy { db!!.sponsorsDao() }
    private var sponsors: List<Sponsor> = emptyList()
    private val repository = SponsorRepository()

    fun fetchSponsors(liveData: MutableLiveData<DataResponse<List<Sponsor>>>?) {
        sponsors = emptyList()
        val finalResponse = DataResponse<List<Sponsor>>(emptyList())

        repository.fetchSponsors { response ->
            if (response.isStatusFailedOrError()) {
                sponsors = fetchSponsorListFromRoom()
                if (sponsors.isEmpty()) liveData?.postValue(finalResponse.apply { setFailureStatus() })
                else liveData?.postValue(finalResponse.apply { updateSuccessValue(sponsors) })
            } else {
                saveAll(response.data)
                AsyncTask.execute {
                    sponsors = fetchSponsorListFromRoom()
                    liveData?.postValue(finalResponse.apply { updateSuccessValue(sponsors) })
                }
            }
        }
    }

    //TODO: change request for RxRoom
    private fun fetchSponsorListFromRoom() = sponsorsDao.getList()

    //TODO: change request for RxRoom
    private fun saveAll(sponsors: List<Sponsor>) {
        AsyncTask.execute {
            sponsorsDao.deleteAll()
        }
        AsyncTask.execute {
            sponsorsDao.save(sponsors)
        }
    }
}