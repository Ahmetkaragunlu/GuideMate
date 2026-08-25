package com.ahmetkaragunlu.guidemate.common.network

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import retrofit2.Response

@Singleton
class ApiCallExecutor
    @Inject
    constructor(
        private val apiErrorParser: ApiErrorParser,
        private val networkExceptionMapper: NetworkExceptionMapper,
    ) {
        suspend fun <ResponseBody, Domain> execute(
            request: suspend () -> Response<ResponseBody>,
            transform: (ResponseBody) -> Domain,
        ): DataResult<Domain> =
            runCatchingRequest {
                val response = request()
                if (!response.isSuccessful) {
                    DataResult.Error(apiErrorParser.parse(response))
                } else {
                    response.body()?.let { DataResult.Success(transform(it)) }
                        ?: DataResult.Error(AppError.NoResponseFromServer)
                }
            }

        suspend fun executeUnit(request: suspend () -> Response<*>): DataResult<Unit> =
            runCatchingRequest {
                val response = request()
                if (response.isSuccessful) {
                    DataResult.Success(Unit)
                } else {
                    DataResult.Error(apiErrorParser.parse(response))
                }
            }

        private suspend fun <T> runCatchingRequest(block: suspend () -> DataResult<T>): DataResult<T> =
            try {
                block()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                DataResult.Error(networkExceptionMapper.map(exception), exception)
            }
    }
