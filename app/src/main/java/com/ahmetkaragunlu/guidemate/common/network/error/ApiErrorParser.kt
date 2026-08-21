package com.ahmetkaragunlu.guidemate.common.network.error

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.AppFieldError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiErrorParser @Inject constructor(
    private val gson: Gson,
) {
    fun parse(response: Response<*>): AppError {
        val errorResponse =
            runCatching {
                response.errorBody()?.string()?.takeIf(String::isNotBlank)?.let {
                    gson.fromJson(it, ApiErrorResponse::class.java)
                }
            }.getOrNull()

        if (errorResponse == null) {
            return AppError.Server(response.code())
        }

        return AppError.Backend(
            code = BackendErrorCode.from(errorResponse.code),
            fallbackMessage = errorResponse.message,
            fieldErrors =
                errorResponse.fieldErrors.map {
                    AppFieldError(
                        field = it.field,
                        code = it.code,
                        fallbackMessage = it.message,
                    )
                },
            retryAfterSeconds = response.headers()["Retry-After"]?.toLongOrNull(),
        )
    }
}
