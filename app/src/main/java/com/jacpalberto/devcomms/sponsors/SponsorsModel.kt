package com.jacpalberto.devcomms.sponsors

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

    fun fetchSponsors(onResult: (response: DataResponse<List<Sponsor>>) -> Unit) {
        sponsors = emptyList()
        val finalResponse = DataResponse<List<Sponsor>>(emptyList())

        repository.fetchSponsors { response ->
            if (response.isStatusFailedOrError()) {
                sponsors = sponsorsDao.getList()
                if (sponsors.isEmpty()) onResult(finalResponse.apply { setFailureStatus() })
                else onResult(finalResponse.apply { updateSuccessValue(sponsors) })
            } else {
                saveAll(response.data)
                AsyncTask.execute {
                    sponsors = sponsorsDao.getList()
                    onResult(finalResponse.apply { updateSuccessValue(sponsors) })
                }
            }
        }
    }

    private fun saveAll(sponsors: List<Sponsor>) {
        AsyncTask.execute {
            sponsorsDao.deleteAll()
        }
        AsyncTask.execute {
            sponsorsDao.save(sponsors)
        }
    }
}