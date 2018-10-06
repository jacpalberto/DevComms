package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.MutableLiveData
import com.jacpalberto.devcomms.DevCommsApp
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.Sponsor
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


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
                fetchSponsorsFromRoom({ list ->
                    liveData?.postValue(finalResponse.apply { updateSuccessValue(list) })
                },
                        { liveData?.postValue(finalResponse.apply { setFailureStatus() }) })
            } else {
                replaceRoomDataForResponse(response.data,
                        { list -> liveData?.postValue(finalResponse.apply { updateSuccessValue(list) }) },
                        { liveData?.postValue(finalResponse.apply { setFailureStatus() }) })
            }
        }
    }

    private fun fetchSponsorsFromRoom(onSuccess: (sponsorList: List<Sponsor>) -> Unit, onError: () -> Unit) {
        sponsorsDao.getList().toObservable()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext { onSuccess(it) }
                .doOnError { onError() }
                .subscribe { }
    }

    private fun replaceRoomDataForResponse(sponsors: List<Sponsor>, onSuccess: (sponsorList: List<Sponsor>) -> Unit, onError: () -> Unit) {
        Single.fromCallable { sponsorsDao.deleteAll() }
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    saveSponsors(sponsors, onSuccess, onError)
                }
    }

    private fun saveSponsors(sponsors: List<Sponsor>, onSuccess: (sponsorList: List<Sponsor>) -> Unit, onError: () -> Unit) {
        Observable.just(sponsors)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ sponsorList ->
                    sponsorsDao.save(sponsorList)
                    fetchSponsorsFromRoom(onSuccess, onError)
                }, { _ -> onError() })
    }
}