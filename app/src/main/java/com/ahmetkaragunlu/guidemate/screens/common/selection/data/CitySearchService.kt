package com.ahmetkaragunlu.guidemate.screens.common.selection.data

import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CityOption
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CitySearchResult

interface CitySearchService {
    fun createSession(countryCode: String): CitySearchSession
}

interface CitySearchSession {
    suspend fun search(query: String): Result<List<CitySearchResult>>

    suspend fun resolve(result: CitySearchResult): Result<CityOption>
}
