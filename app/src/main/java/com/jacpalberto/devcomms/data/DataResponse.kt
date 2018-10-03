package com.jacpalberto.devcomms.data

/**
 * Created by Alberto Carrillo on 10/3/18.
 */
class DataResponse<T>(t: T, var status: DataState = DataState.NONE) {
    var error: Int = 0
    var data: T = t

    fun setErrorStatus(errorCode: Int) {
        status = DataState.ERROR
        error = errorCode
    }

    fun setFailureStatus() {
        status = DataState.FAILURE
    }

    fun updateSuccessValue(newValue: T) {
        data = newValue
        status = DataState.SUCCESS
        error = 0
    }

    fun isStatusFailedOrError(): Boolean = (status == DataState.ERROR || status == DataState.FAILURE)

}