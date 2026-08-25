package com.ahmetkaragunlu.guidemate.common.location.search

import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CitySearchResult

interface CitySearchService {
    fun createSession(countryCode: String): CitySearchSession
}

interface CitySearchSession {
    suspend fun search(query: String): Result<List<CitySearchResult>>

    suspend fun resolve(result: CitySearchResult): Result<CityOption>
}
