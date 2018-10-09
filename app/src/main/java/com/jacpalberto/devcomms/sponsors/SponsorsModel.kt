package com.jacpalberto.devcomms.sponsors

import androidx.lifecycle.MutableLiveData
import com.jacpalberto.devcomms.DevCommsApp
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.Sponsor
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alberto Carrillo on 8/20/18.
 */
class SponsorsModel {
    //TODO: inject this properties
    private val db by lazy { DevCommsApp.database }
    private val sponsorsDao by lazy { db!!.sponsorsDao() }
    private val repository = SponsorRepository()

    private val compositeDisposable = CompositeDisposable()

    fun fetchSponsors(liveData: MutableLiveData<DataResponse<List<Sponsor>>>?) {
        val finalResponse = DataResponse<List<Sponsor>>(emptyList())

        repository.fetchSponsors { response ->
            if (response.isStatusFailedOrError()) fetchSponsorsFromRoom(
                    { liveData?.postValue(finalResponse.apply { updateSuccessValue(it) }) },
                    { liveData?.postValue(finalResponse.apply { setFailureStatus() }) })
            else replaceRoomDataForResponse(response.data,
                    { liveData?.postValue(finalResponse.apply { updateSuccessValue(it) }) },
                    { liveData?.postValue(finalResponse.apply { setFailureStatus() }) })
        }
    }

    private fun replaceRoomDataForResponse(sponsors: List<Sponsor>, onSuccess: (sponsorList: List<Sponsor>) -> Unit, onError: () -> Unit) {
        Single.fromCallable { sponsorsDao.deleteAll() }
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ saveSponsors(sponsors, onSuccess, onError) }) { onError() }
                .addTo(compositeDisposable)
    }

    private fun saveSponsors(sponsors: List<Sponsor>, onSuccess: (sponsorList: List<Sponsor>) -> Unit, onError: () -> Unit) {
        Single.fromCallable { sponsorsDao.save(sponsors) }
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ fetchSponsorsFromRoom(onSuccess, onError) }) { onError() }
                .addTo(compositeDisposable)
    }

    private fun fetchSponsorsFromRoom(onSuccess: (sponsorList: List<Sponsor>) -> Unit, onError: () -> Unit) {
        sponsorsDao.getList().toObservable()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess(it) }) { onError() }
                .addTo(compositeDisposable)
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }
}