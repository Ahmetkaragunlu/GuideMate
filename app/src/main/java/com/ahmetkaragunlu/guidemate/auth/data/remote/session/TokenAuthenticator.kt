package com.ahmetkaragunlu.guidemate.auth.data.remote.session

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.auth.domain.session.isTerminalSessionError
import com.ahmetkaragunlu.guidemate.auth.data.local.session.AuthSessionManager
import com.ahmetkaragunlu.guidemate.auth.data.local.session.TokenManager
import com.ahmetkaragunlu.guidemate.auth.data.remote.api.AuthApi
import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.storage.installation.InstallationIdDataSource
import com.ahmetkaragunlu.guidemate.auth.data.remote.session.TokenRefreshException
import com.ahmetkaragunlu.guidemate.auth.data.remote.model.request.RefreshTokenRequest
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val installationIdDataSource: InstallationIdDataSource,
    private val authSessionManager: AuthSessionManager,
    private val authApiProvider: Provider<AuthApi>,
    private val apiErrorParser: ApiErrorParser,
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        val path = response.request.url.encodedPath
        if (
            response.code != 401 ||
                AuthEndpointPolicy.isRefreshRequest(path) ||
                !AuthEndpointPolicy.requiresAccessToken(path) ||
                responseCount(response) > MAX_RETRY_COUNT
        ) {
            return null
        }

        synchronized(this) {
            val requestAccessToken = response.request.bearerToken()
            val currentAccessToken = tokenManager.getAccessToken()
            if (!currentAccessToken.isNullOrBlank() && currentAccessToken != requestAccessToken) {
                return response.request.withBearerToken(currentAccessToken)
            }

            val refreshToken =
                tokenManager.getRefreshToken()
                    ?: return clearSessionAndStop()

            return try {
                val refreshResponse =
                    authApiProvider
                        .get()
                        .refreshTokenSync(
                            request = RefreshTokenRequest(refreshToken),
                            installationId = installationIdDataSource.getOrCreateBlocking(),
                        ).execute()

                if (refreshResponse.isSuccessful) {
                    val body = refreshResponse.body()
                    val accessToken = body?.accessToken
                    val newRefreshToken = body?.refreshToken
                    if (accessToken.isNullOrBlank() || newRefreshToken.isNullOrBlank()) {
                        return clearSessionAndStop()
                    }
                    tokenManager.saveTokens(accessToken, newRefreshToken)
                    response.request.withBearerToken(accessToken)
                } else {
                    val error = apiErrorParser.parse(refreshResponse)
                    if (refreshResponse.code() == 401 || error.isTerminalSessionError()) {
                        authSessionManager.clearSessionBlocking()
                        null
                    } else {
                        throw TokenRefreshException(error)
                    }
                }
            } catch (exception: TokenRefreshException) {
                throw exception
            } catch (exception: SocketTimeoutException) {
                throw TokenRefreshException(AppError.NoResponseFromServer, exception)
            } catch (exception: UnknownHostException) {
                throw TokenRefreshException(AppError.NoInternet, exception)
            } catch (exception: IOException) {
                throw TokenRefreshException(AppError.NoInternet, exception)
            } catch (exception: Exception) {
                throw TokenRefreshException(AppError.Unknown, exception)
            }
        }
    }

    private fun clearSessionAndStop(): Request? {
        authSessionManager.clearSessionBlocking()
        return null
    }

    private fun Request.bearerToken(): String? =
        header("Authorization")?.removePrefix(BEARER_PREFIX)

    private fun Request.withBearerToken(token: String): Request =
        newBuilder().header("Authorization", "$BEARER_PREFIX$token").build()

    private fun responseCount(response: Response): Int {
        var current: Response? = response
        var count = 1
        while (current?.priorResponse != null) {
            count++
            current = current.priorResponse
        }
        return count
    }

    private companion object {
        const val BEARER_PREFIX = "Bearer "
        const val MAX_RETRY_COUNT = 1
    }
}
