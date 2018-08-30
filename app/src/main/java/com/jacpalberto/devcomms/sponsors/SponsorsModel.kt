package com.jacpalberto.devcomms.sponsors

import android.os.AsyncTask
import android.util.Log
import com.jacpalberto.devcomms.DevCommsApp
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.FirebaseRepository
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.data.SponsorList

/**
 * Created by Alberto Carrillo on 8/20/18.
 */
object SponsorsModel {
    private val db by lazy { DevCommsApp.database }
    private val sponsorsDao by lazy { db!!.sponsorsDao() }
    private var sponsorsList: List<Sponsor> = emptyList()

    fun fetchSponsors(onResult: (SponsorList) -> Unit) {
        lateinit var sponsorResult: SponsorList
        FirebaseRepository.fetchSponsors { response ->
            if (response.status == DataState.FAILURE || response.status == DataState.ERROR) {
                sponsorsList = sponsorsDao.getList()
                sponsorResult = if (sponsorsList.isEmpty())
                    SponsorList(sponsorsList, status = DataState.FAILURE)
                else SponsorList(sponsorsList, status = DataState.SUCCESS)
                onResult(sponsorResult)
            } else {
                saveAll(response.sponsorList)
                AsyncTask.execute {
                    sponsorsList = sponsorsDao.getList()
                    sponsorResult = SponsorList(sponsorsList, status = DataState.SUCCESS)
                    onResult(sponsorResult)
                }
            }
        }
    }

    private fun saveAll(sponsors: List<Sponsor>) {
        AsyncTask.execute {
            sponsorsDao.deleteAll()
            sponsorsDao.save(sponsors) }
    }
}