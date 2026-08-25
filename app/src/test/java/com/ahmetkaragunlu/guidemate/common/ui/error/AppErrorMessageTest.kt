package com.ahmetkaragunlu.guidemate.common.ui.error

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.AppFieldError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import org.junit.Assert.assertEquals
import org.junit.Test

class AppErrorMessageTest {
    private val resourceProvider = ResourceIdProvider()

    @Test
    fun `backend errors keep centralized mappings across feature groups`() {
        val mappings =
            mapOf(
                BackendErrorCode.INVALID_CREDENTIALS to R.string.error_invalid_credentials,
                BackendErrorCode.MEDIA_NOT_FOUND to R.string.error_media_not_found,
                BackendErrorCode.SESSION_NOT_BOOKABLE to R.string.error_session_not_bookable,
                BackendErrorCode.PAYMENT_NOT_FOUND to R.string.error_payment_not_found,
                BackendErrorCode.CHAT_NOT_FOUND to R.string.error_chat_not_found,
            )

        mappings.forEach { (code, expectedResource) ->
            assertEquals(
                expectedResource.toString(),
                AppError.Backend(code, fallbackMessage = null).toMessage(resourceProvider),
            )
        }
    }

    @Test
    fun `unknown backend code keeps generic fallback`() {
        assertEquals(
            R.string.error_generic_failure.toString(),
            AppError.Backend(code = null, fallbackMessage = null).toMessage(resourceProvider),
        )
    }

    @Test
    fun `field errors keep their existing resource mappings`() {
        val error =
            AppError.Backend(
                code = BackendErrorCode.VALIDATION_FAILED,
                fallbackMessage = null,
                fieldErrors =
                    listOf(
                        AppFieldError(
                            field = "email",
                            code = "INVALID_EMAIL",
                            fallbackMessage = null,
                        ),
                    ),
            )

        assertEquals(
            R.string.email_error_message.toString(),
            error.fieldMessage("email", resourceProvider),
        )
    }

    private class ResourceIdProvider : ResourceProvider {
        override fun getString(id: Int): String = id.toString()

        override fun getString(
            id: Int,
            vararg args: Any,
        ): String = id.toString()

        override fun getQuantityString(
            id: Int,
            quantity: Int,
            vararg args: Any,
        ): String = id.toString()
    }
}
