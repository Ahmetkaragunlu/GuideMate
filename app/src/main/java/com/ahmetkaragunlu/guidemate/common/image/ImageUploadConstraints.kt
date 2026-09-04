package com.ahmetkaragunlu.guidemate.common.image

import java.io.InputStream

const val MAX_IMAGE_UPLOAD_BYTES = 5L * 1024 * 1024
const val MAX_IMAGE_UPLOAD_DIMENSION_PX = 2048
const val IMAGE_UPLOAD_JPEG_QUALITY = 85
const val IMAGE_SIGNATURE_LENGTH = 12

fun detectSupportedImageMimeType(signature: ByteArray): String? =
    when {
        signature.isJpeg() -> "image/jpeg"
        signature.isPng() -> "image/png"
        signature.isWebp() -> "image/webp"
        else -> null
    }

fun InputStream.readImageSignature(): ByteArray {
    val signature = ByteArray(IMAGE_SIGNATURE_LENGTH)
    var bytesRead = 0
    while (bytesRead < signature.size) {
        val count = read(signature, bytesRead, signature.size - bytesRead)
        if (count < 0) break
        bytesRead += count
    }
    return signature.copyOf(bytesRead)
}

private fun ByteArray.isJpeg(): Boolean =
    size >= 3 && unsigned(0) == 0xFF && unsigned(1) == 0xD8 && unsigned(2) == 0xFF

private fun ByteArray.isPng(): Boolean {
    val pngSignature = intArrayOf(0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
    return size >= pngSignature.size &&
        pngSignature.indices.all { index -> unsigned(index) == pngSignature[index] }
}

private fun ByteArray.isWebp(): Boolean =
    size >= IMAGE_SIGNATURE_LENGTH &&
        this[0] == 'R'.code.toByte() &&
        this[1] == 'I'.code.toByte() &&
        this[2] == 'F'.code.toByte() &&
        this[3] == 'F'.code.toByte() &&
        this[8] == 'W'.code.toByte() &&
        this[9] == 'E'.code.toByte() &&
        this[10] == 'B'.code.toByte() &&
        this[11] == 'P'.code.toByte()

private fun ByteArray.unsigned(index: Int): Int = this[index].toInt() and 0xFF
