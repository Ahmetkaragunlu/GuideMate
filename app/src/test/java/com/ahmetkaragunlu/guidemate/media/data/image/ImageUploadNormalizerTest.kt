package com.ahmetkaragunlu.guidemate.media.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.ExifInterface
import android.net.Uri
import com.ahmetkaragunlu.guidemate.common.image.MAX_IMAGE_UPLOAD_BYTES
import com.ahmetkaragunlu.guidemate.common.image.MAX_IMAGE_UPLOAD_DIMENSION_PX
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.media.data.multipart.MediaPreparationException
import java.io.File
import java.io.FileOutputStream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ImageUploadNormalizerTest {
    private val context: Context = RuntimeEnvironment.getApplication()
    private val normalizer = ImageUploadNormalizer(context)

    @Before
    fun clearNormalizedUploadDirectory() {
        normalizedUploadFiles().forEach(File::delete)
    }

    @Test
    fun `normalizes large image as bounded jpeg upload copy`() {
        val source = createJpeg(width = 3000, height = 1200)

        val normalized = normalizer.normalize(Uri.fromFile(source).toString())

        val output = BitmapFactory.decodeFile(normalized.absolutePath)
        try {
            assertEquals(MAX_IMAGE_UPLOAD_DIMENSION_PX, maxOf(output.width, output.height))
            assertTrue(normalized.length() in 1..MAX_IMAGE_UPLOAD_BYTES)
            assertTrue(normalized.inputStream().use { it.read() == 0xFF && it.read() == 0xD8 })
            assertTrue(source.exists())
        } finally {
            output.recycle()
            normalized.delete()
            source.delete()
        }
    }

    @Test
    fun `applies exif orientation before creating upload copy`() {
        val source = createJpeg(width = 40, height = 20)
        ExifInterface(source.absolutePath).apply {
            setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90.toString())
            saveAttributes()
        }

        val normalized = normalizer.normalize(Uri.fromFile(source).toString())

        val output = BitmapFactory.decodeFile(normalized.absolutePath)
        try {
            assertEquals(20, output.width)
            assertEquals(40, output.height)
        } finally {
            output.recycle()
            normalized.delete()
            source.delete()
        }
    }

    @Test
    fun `rejects unsupported source without creating upload copy`() {
        val source = File.createTempFile("not_an_image_", ".txt", context.cacheDir)
        source.writeText("not an image")

        val exception =
            runCatching { normalizer.normalize(Uri.fromFile(source).toString()) }.exceptionOrNull()

        assertTrue(exception is MediaPreparationException)
        assertSame(AppError.InvalidImageType, (exception as MediaPreparationException).error)
        assertTrue(normalizedUploadFiles().isEmpty())
        source.delete()
    }

    private fun createJpeg(
        width: Int,
        height: Int,
    ): File {
        val file = File.createTempFile("source_", ".jpg", context.cacheDir)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.rgb(45, 120, 180))
        FileOutputStream(file).use { output ->
            assertTrue(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output))
        }
        bitmap.recycle()
        return file
    }

    private fun normalizedUploadFiles(): List<File> =
        File(context.cacheDir, "normalized_uploads").listFiles()?.toList().orEmpty()
}
