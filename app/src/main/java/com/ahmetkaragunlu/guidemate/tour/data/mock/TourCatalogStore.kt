package com.ahmetkaragunlu.guidemate.tour.data.mock

import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourCatalogState
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class TourCatalogStore
    @Inject
    constructor() {
        private val _state = MutableStateFlow(createMockTourCatalogState())
        val state: StateFlow<TourCatalogState> = _state.asStateFlow()
    }
