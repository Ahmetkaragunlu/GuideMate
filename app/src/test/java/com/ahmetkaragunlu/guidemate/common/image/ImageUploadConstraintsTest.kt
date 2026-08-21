package com.ahmetkaragunlu.guidemate.common.image

import java.io.ByteArrayInputStream
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ImageUploadConstraintsTest {
    @Test
    fun `detects supported image signatures`() {
        assertEquals("image/jpeg", detectSupportedImageMimeType(byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())))
        assertEquals(
            "image/png",
            detectSupportedImageMimeType(
                byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A),
            ),
        )
        assertEquals(
            "image/webp",
            detectSupportedImageMimeType("RIFF0000WEBP".encodeToByteArray()),
        )
    }

    @Test
    fun `rejects unsupported image signatures`() {
        assertNull(detectSupportedImageMimeType("not-an-image".encodeToByteArray()))
    }

    @Test
    fun `reads only the bytes required for image detection`() {
        val source = ByteArray(IMAGE_SIGNATURE_LENGTH + 5) { it.toByte() }

        val result = ByteArrayInputStream(source).readImageSignature()

        assertEquals(IMAGE_SIGNATURE_LENGTH, result.size)
        assertArrayEquals(source.copyOf(IMAGE_SIGNATURE_LENGTH), result)
    }
}
