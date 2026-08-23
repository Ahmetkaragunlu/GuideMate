package com.ahmetkaragunlu.guidemate.payment.presentation.hosted

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

@Composable
fun HostedPaymentScreen(
    onVerificationRequired: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HostedPaymentViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { }
    LaunchedEffect(uiState.shouldVerifyPayment) {
        if (uiState.shouldVerifyPayment) onVerificationRequired()
    }

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::loadPaymentPage,
        modifier = modifier,
        errorMessage = uiState.pageErrorMessage,
    ) {
        val paymentPageUrl = uiState.paymentPageUrl
        if (paymentPageUrl != null) {
            HostedPaymentContent(
                paymentPageUrl = paymentPageUrl,
                reloadToken = uiState.reloadToken,
                isPageLoading = uiState.isPageLoading,
                pageErrorMessage = uiState.pageErrorMessage,
                isCancelling = uiState.isCancelling,
                onPageFinished = viewModel::onPageFinished,
                onPageError = viewModel::onPageError,
                onRetryPage = viewModel::retryPage,
                onCancelPayment = viewModel::cancelPayment,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun HostedPaymentContent(
    paymentPageUrl: String,
    reloadToken: Int,
    isPageLoading: Boolean,
    pageErrorMessage: String?,
    isCancelling: Boolean,
    onPageFinished: () -> Unit,
    onPageError: (String) -> Unit,
    onRetryPage: () -> Unit,
    onCancelPayment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var webView by remember { mutableStateOf<WebView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            webView?.run {
                stopLoading()
                loadUrl("about:blank")
                clearHistory()
                removeAllViews()
                destroy()
            }
            webView = null
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.allowFileAccess = false
                        settings.allowContentAccess = false
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                        webViewClient =
                            object : WebViewClient() {
                                override fun onPageStarted(
                                    view: WebView?,
                                    url: String?,
                                    favicon: Bitmap?,
                                ) = Unit

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    onPageFinished()
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?,
                                ) {
                                    if (request?.isForMainFrame == true) {
                                        onPageError(
                                            error?.description?.toString()
                                                ?: context.getString(R.string.payment_page_load_error),
                                        )
                                    }
                                }

                                override fun onReceivedSslError(
                                    view: WebView?,
                                    handler: SslErrorHandler?,
                                    error: SslError?,
                                ) {
                                    handler?.cancel()
                                    onPageError(context.getString(R.string.payment_ssl_error))
                                }
                            }
                        tag = reloadToken
                        loadUrl(paymentPageUrl)
                        webView = this
                    }
                },
                update = { view ->
                    if (view.tag != reloadToken) {
                        view.tag = reloadToken
                        view.loadUrl(paymentPageUrl)
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )

            if (isPageLoading) {
                GuideMateContentState(
                    state = ContentLoadState.LOADING,
                    onRetry = {},
                    modifier = Modifier.fillMaxSize().background(Color.White),
                ) {}
            }

            if (pageErrorMessage != null) {
                GuideMateContentState(
                    state = ContentLoadState.ERROR,
                    onRetry = onRetryPage,
                    modifier = Modifier.fillMaxSize().background(Color.White),
                    errorMessage = pageErrorMessage,
                ) {}
            }
        }

        EditButton(
            text = R.string.cancel_payment,
            onClick = onCancelPayment,
            enabled = !isCancelling,
            modifier =
                Modifier.padding(
                    horizontal = dimensionResource(R.dimen.spacing_medium),
                    vertical = dimensionResource(R.dimen.spacing_small),
                ),
        )
    }
}
