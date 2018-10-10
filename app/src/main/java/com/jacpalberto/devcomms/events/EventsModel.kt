package com.jacpalberto.devcomms.events

import androidx.lifecycle.MutableLiveData
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.DevCommsEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

/**
 * Created by Alberto Carrillo on 8/30/18.
 */
class EventsModel(private val repository: EventsRepository, private val eventsDao: EventsDao) {
    private val compositeDisposable = CompositeDisposable()

    fun fetchEvents(liveData: MutableLiveData<DataResponse<List<DevCommsEvent>>>) {
        val finalResponse = DataResponse<List<DevCommsEvent>>(emptyList(), DataState.SUCCESS)

        repository.fetchEvents { response ->
            if (response.isStatusFailedOrError()) fetchEventsFromRoom(
                    { liveData.postValue(finalResponse.apply { updateSuccessValue(it) }) },
                    { liveData.postValue(finalResponse.apply { setFailureStatus() }) })
            else {
                fetchFavoriteEventsFromRoom { list ->
                    updateFavoriteFields(list, response.data)
                    saveEvents(response.data,
                            { liveData.postValue(finalResponse.apply { updateSuccessValue(it) }) },
                            { liveData.postValue(finalResponse.apply { setFailureStatus() }) })
                }
            }
        }
    }

    fun fetchFavoriteEvents(liveData: MutableLiveData<DataResponse<List<DevCommsEvent>>>) {
        fetchFavoriteEventsFromRoom { liveData.postValue(DataResponse(it, DataState.SUCCESS)) }
    }

    fun toggleFavorite(key: String, isFavorite: Boolean) {
        Single.fromCallable { eventsDao.updateFavorite(key, isFavorite) }
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
                .addTo(compositeDisposable)
    }

    private fun saveEvents(events: List<DevCommsEvent>, onSuccess: (eventList: List<DevCommsEvent>) -> Unit, onError: () -> Unit) {
        Single.fromCallable { eventsDao.save(events) }
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ fetchEventsFromRoom(onSuccess, onError) }) { onError() }
                .addTo(compositeDisposable)
    }

    private fun fetchEventsFromRoom(onSuccess: (eventList: List<DevCommsEvent>) -> Unit, onError: () -> Unit) {
        eventsDao.getList().toObservable()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess(it) }) { onError() }
                .addTo(compositeDisposable)
    }

    private fun fetchFavoriteEventsFromRoom(onSuccess: (eventList: List<DevCommsEvent>) -> Unit) {
        eventsDao.getFavoriteList().toObservable()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { onSuccess(it) }
                .addTo(compositeDisposable)
    }

    private fun updateFavoriteFields(favoriteList: List<DevCommsEvent>, eventList: List<DevCommsEvent>) {
        for (item in favoriteList) {
            for (event in eventList) {
                if (event.key == item.key) {
                    event.isFavorite = true
                    break
                }
            }
        }
    }
}
