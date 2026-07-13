package com.ahmetkaragunlu.guidemate.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.ahmetkaragunlu.guidemate.R
import java.io.File

private const val MAX_IMAGE_BYTES = 10L * 1024 * 1024
private const val CAMERA_IMAGE_DIRECTORY = "selected_images"

@Composable
fun ImageSourcePicker(
    isVisible: Boolean,
    @StringRes titleResId: Int,
    onDismissRequest: () -> Unit,
    onImageSelected: (String) -> Unit,
    onError: (Int) -> Unit,
) {
    val context = LocalContext.current
    var pendingCameraUri by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingCameraFilePath by rememberSaveable { mutableStateOf<String?>(null) }

    val handleSelectedImage: (Uri) -> Boolean = { uri ->
        val isValid = context.isImageWithinSizeLimit(uri)
        if (isValid) {
            onImageSelected(uri.toString())
        } else {
            onError(R.string.error_image_too_large)
        }
        isValid
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let(handleSelectedImage)
        }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
            val capturedImageUri = pendingCameraUri?.toUri()
            val shouldKeepImage =
                isSaved && capturedImageUri != null && handleSelectedImage(capturedImageUri)
            if (!shouldKeepImage) {
                pendingCameraFilePath?.let(::File)?.delete()
            }
            pendingCameraUri = null
            pendingCameraFilePath = null
        }

    if (!isVisible) return

    ImageSourceBottomSheet(
        titleResId = titleResId,
        onDismiss = onDismissRequest,
        onGalleryClick = {
            onDismissRequest()
            runCatching {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                }.onFailure { onError(R.string.error_image_source_unavailable) }
        },
        onCameraClick = {
            onDismissRequest()
            val pendingImage =
                runCatching { context.createPendingCameraImage() }
                    .getOrElse {
                        onError(R.string.error_image_source_unavailable)
                        return@ImageSourceBottomSheet
                    }
            pendingCameraUri = pendingImage.uri.toString()
            pendingCameraFilePath = pendingImage.file.absolutePath
            runCatching { cameraLauncher.launch(pendingImage.uri) }
                .onFailure {
                    pendingImage.file.delete()
                    pendingCameraUri = null
                    pendingCameraFilePath = null
                    onError(R.string.error_image_source_unavailable)
                }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageSourceBottomSheet(
    @StringRes titleResId: Int,
    onDismiss: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        ) {
            Text(
                text = stringResource(titleResId),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen.spacing_small)),
            )
            ListItem(
                headlineContent = { Text(text = stringResource(R.string.choose_from_gallery)) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        tint = colorResource(R.color.brand_color),
                    )
                },
                modifier = Modifier.clickable(onClick = onGalleryClick),
            )
            ListItem(
                headlineContent = { Text(text = stringResource(R.string.take_photo)) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = colorResource(R.color.brand_color),
                    )
                },
                modifier = Modifier.clickable(onClick = onCameraClick),
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        }
    }
}

private data class PendingCameraImage(
    val uri: Uri,
    val file: File,
)

private fun Context.createPendingCameraImage(): PendingCameraImage {
    val imageDirectory = File(cacheDir, CAMERA_IMAGE_DIRECTORY).apply { mkdirs() }
    val imageFile = File.createTempFile("selected_image_", ".jpg", imageDirectory)
    val imageUri =
        FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            imageFile,
        )
    return PendingCameraImage(uri = imageUri, file = imageFile)
}

private fun Context.isImageWithinSizeLimit(uri: Uri): Boolean =
    runCatching {
            contentResolver.openAssetFileDescriptor(uri, "r")?.use { descriptor ->
                descriptor.length < 0 || descriptor.length <= MAX_IMAGE_BYTES
            } ?: false
        }.getOrDefault(false)
