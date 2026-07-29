package com.ahmetkaragunlu.guidemate.domain.usecase

import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import javax.inject.Inject

class ClearSessionUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke() {
        repository.clearLocalSession()
    }
}
