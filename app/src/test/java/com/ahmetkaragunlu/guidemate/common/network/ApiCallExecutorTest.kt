package com.ahmetkaragunlu.guidemate.common.network

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.google.gson.Gson
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ApiCallExecutorTest {
    private val executor = ApiCallExecutor(ApiErrorParser(Gson()), NetworkExceptionMapper())

    @Test
    fun `successful response is transformed once`() = runBlocking {
        val result = executor.execute(request = { Response.success(21) }, transform = { it * 2 })

        assertEquals(42, (result as DataResult.Success).data)
    }

    @Test
    fun `empty successful response is not treated as content`() = runBlocking {
        val result =
            executor.execute<String, String>(
                request = { Response.success(null) },
                transform = String::uppercase,
            )

        assertEquals(AppError.NoResponseFromServer, (result as DataResult.Error).error)
    }

    @Test
    fun `backend error keeps structured code`() = runBlocking {
        val body =
            """{"code":"VALIDATION_FAILED","message":"invalid","fieldErrors":[]}"""
                .toResponseBody("application/json".toMediaType())

        val result =
            executor.execute<String, String>(
                request = { Response.error(400, body) },
                transform = { it },
            )

        val error = (result as DataResult.Error).error as AppError.Backend
        assertEquals(BackendErrorCode.VALIDATION_FAILED, error.code)
    }

    @Test
    fun `network exception is mapped while cancellation is rethrown`() {
        val networkResult = runBlocking {
            executor.execute<String, String>(
                request = { throw UnknownHostException() },
                transform = { it },
            )
        }
        assertEquals(AppError.NoInternet, (networkResult as DataResult.Error).error)

        assertThrows(CancellationException::class.java) {
            runBlocking {
                executor.execute<String, String>(
                    request = { throw CancellationException() },
                    transform = { it },
                )
            }
        }
    }
}
