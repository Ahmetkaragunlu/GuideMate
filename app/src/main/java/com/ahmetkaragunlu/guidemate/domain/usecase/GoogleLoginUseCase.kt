package com.ahmetkaragunlu.guidemate.domain.usecase

import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.domain.AuthRepository
import com.ahmetkaragunlu.guidemate.domain.model.AuthResult
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): DataResult<AuthResult> =
        repository.googleLogin(idToken)
}