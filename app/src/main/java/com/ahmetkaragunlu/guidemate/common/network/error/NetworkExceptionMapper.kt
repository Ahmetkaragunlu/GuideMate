package com.ahmetkaragunlu.guidemate.common.network.error

import com.ahmetkaragunlu.guidemate.common.result.AppError
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException

interface NetworkErrorCarrier {
    val error: AppError
}

@Singleton
class NetworkExceptionMapper @Inject constructor() {
    fun map(exception: Exception): AppError =
        when (exception) {
            is NetworkErrorCarrier -> exception.error
            is ConnectException,
            is SocketTimeoutException,
            -> AppError.NoResponseFromServer
            is UnknownHostException -> AppError.NoInternet
            is IOException -> AppError.NoInternet
            is HttpException -> AppError.Server(exception.code())
            else -> AppError.Unknown
        }
}
