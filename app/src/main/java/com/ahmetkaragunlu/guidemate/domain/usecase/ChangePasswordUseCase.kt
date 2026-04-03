package com.ahmetkaragunlu.guidemate.domain.usecase

import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): DataResult<String> =
        repository.changePassword(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmPassword = confirmPassword,
        )
}
