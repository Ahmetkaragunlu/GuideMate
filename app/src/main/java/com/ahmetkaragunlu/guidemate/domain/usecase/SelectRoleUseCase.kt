package com.ahmetkaragunlu.guidemate.domain.usecase

import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.domain.model.AuthResult
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import javax.inject.Inject

class SelectRoleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(role: UserRole): DataResult<AuthResult> =
        repository.selectRole(role)
}