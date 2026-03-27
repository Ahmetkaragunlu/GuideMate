package com.ahmetkaragunlu.guidemate.domain.usecase

import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        firstName: String,
        lastName: String
    ): DataResult<String> = repository.forgotPassword(email, firstName, lastName)
}