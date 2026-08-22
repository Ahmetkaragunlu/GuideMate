package com.ahmetkaragunlu.guidemate.payment.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.payment.data.remote.api.SavedPaymentMethodApi
import com.ahmetkaragunlu.guidemate.payment.domain.model.SavedPaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.Response

class SavedPaymentMethodRepositoryImpl @Inject constructor(
    private val api: SavedPaymentMethodApi,
    private val apiErrorParser: ApiErrorParser,
    private val networkExceptionMapper: NetworkExceptionMapper,
) : SavedPaymentMethodRepository {
    private val mutablePaymentMethodChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val paymentMethodChanges: Flow<Unit> = mutablePaymentMethodChanges.asSharedFlow()

    override suspend fun getSavedPaymentMethods(): DataResult<List<SavedPaymentMethod>> =
        execute(
            request = api::getCards,
            transform = { cards -> cards.map { it.toDomain() } },
        )

    override suspend fun makeDefault(
        savedPaymentMethodId: String,
    ): DataResult<SavedPaymentMethod> =
        execute(
            request = { api.makeDefault(savedPaymentMethodId) },
            transform = { it.toDomain() },
        ).also(::emitChangeOnSuccess)

    override suspend fun delete(savedPaymentMethodId: String): DataResult<Unit> =
        try {
            val response = api.delete(savedPaymentMethodId)
            if (response.isSuccessful) {
                DataResult.Success(Unit).also(::emitChangeOnSuccess)
            } else {
                DataResult.Error(apiErrorParser.parse(response))
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            DataResult.Error(networkExceptionMapper.map(exception), exception)
        }

    private fun emitChangeOnSuccess(result: DataResult<*>) {
        if (result is DataResult.Success) mutablePaymentMethodChanges.tryEmit(Unit)
    }

    private suspend fun <ResponseBody, Domain> execute(
        request: suspend () -> Response<ResponseBody>,
        transform: (ResponseBody) -> Domain,
    ): DataResult<Domain> =
        try {
            val response = request()
            if (!response.isSuccessful) {
                DataResult.Error(apiErrorParser.parse(response))
            } else {
                response.body()?.let { DataResult.Success(transform(it)) }
                    ?: DataResult.Error(AppError.NoResponseFromServer)
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            DataResult.Error(networkExceptionMapper.map(exception), exception)
        }
}
