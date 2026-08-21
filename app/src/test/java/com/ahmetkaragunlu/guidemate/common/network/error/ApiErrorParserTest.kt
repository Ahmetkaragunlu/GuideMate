package com.ahmetkaragunlu.guidemate.common.network.error

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ApiErrorParserTest {
    private val parser = ApiErrorParser(Gson())

    @Test
    fun `parse handles omitted field errors`() {
        val response =
            errorResponse(
                """{"code":"INVALID_CREDENTIALS","message":"Invalid credentials","timestamp":"2026-08-21T00:00:00Z"}""",
            )

        val error = parser.parse(response) as AppError.Backend

        assertEquals(BackendErrorCode.INVALID_CREDENTIALS, error.code)
        assertTrue(error.fieldErrors.isEmpty())
    }

    @Test
    fun `parse maps validation field errors`() {
        val response =
            errorResponse(
                """{"code":"VALIDATION_FAILED","message":"Validation failed","timestamp":"2026-08-21T00:00:00Z","fieldErrors":[{"field":"email","code":"NotBlank","message":"Email is required"}]}""",
            )

        val error = parser.parse(response) as AppError.Backend

        assertEquals(BackendErrorCode.VALIDATION_FAILED, error.code)
        assertEquals("email", error.fieldErrors.single().field)
        assertEquals("NotBlank", error.fieldErrors.single().code)
    }

    private fun errorResponse(json: String): Response<Unit> =
        Response.error(
            400,
            json.toResponseBody("application/json".toMediaType()),
        )
}
