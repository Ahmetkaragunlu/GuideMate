package com.ahmetkaragunlu.guidemate.data.remote.places

import android.content.Context
import com.ahmetkaragunlu.guidemate.screens.common.selection.data.CitySearchService
import com.ahmetkaragunlu.guidemate.screens.common.selection.data.CitySearchSession
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CityOption
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CitySearchResult
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine

class GooglePlacesCitySearchService
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) : CitySearchService {
        private val placesClient =
            if (Places.isInitialized()) {
                Places.createClient(context)
            } else {
                null
            }

        override fun createSession(countryCode: String): CitySearchSession =
            GooglePlacesCitySearchSession(
                countryCode = countryCode.uppercase(Locale.ROOT),
                placesClient = placesClient,
            )
    }

private class GooglePlacesCitySearchSession(
    private val countryCode: String,
    private val placesClient: PlacesClient?,
) : CitySearchSession {
    private val sessionToken = AutocompleteSessionToken.newInstance()

    override suspend fun search(query: String): Result<List<CitySearchResult>> =
        runCatchingPreservingCancellation {
            val client = checkNotNull(placesClient) { "Google Places is not initialized" }
            val request =
                FindAutocompletePredictionsRequest.builder()
                    .setQuery(query.trim())
                    .setCountries(listOf(countryCode))
                    .setTypesFilter(listOf(PlaceTypes.CITIES))
                    .setSessionToken(sessionToken)
                    .build()

            client
                .findAutocompletePredictions(request)
                .await()
                .autocompletePredictions
                .map(AutocompletePrediction::toCitySearchResult)
        }

    override suspend fun resolve(result: CitySearchResult): Result<CityOption> =
        runCatchingPreservingCancellation {
            val client = checkNotNull(placesClient) { "Google Places is not initialized" }
            val request =
                FetchPlaceRequest.builder(
                        result.placeId,
                        listOf(
                            Place.Field.ID,
                            Place.Field.DISPLAY_NAME,
                        ),
                    )
                    .setSessionToken(sessionToken)
                    .build()
            val place = client.fetchPlace(request).await().place
            CityOption(
                placeId = checkNotNull(place.id),
                displayName = place.displayName ?: result.primaryText,
                countryCode = countryCode,
            )
        }
}

private fun AutocompletePrediction.toCitySearchResult(): CitySearchResult =
    CitySearchResult(
        placeId = placeId,
        primaryText = getPrimaryText(null).toString(),
        secondaryText = getSecondaryText(null).toString(),
    )

private suspend fun <T> Task<T>.await(): T =
    suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result -> continuation.resume(result) }
        addOnFailureListener { error -> continuation.resumeWithException(error) }
    }

private suspend fun <T> runCatchingPreservingCancellation(
    block: suspend () -> T,
): Result<T> =
    try {
        Result.success(block())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Exception) {
        Result.failure(error)
    }
