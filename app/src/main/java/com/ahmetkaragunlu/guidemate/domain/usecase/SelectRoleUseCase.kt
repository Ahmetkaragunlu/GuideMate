package com.ahmetkaragunlu.guidemate.domain.usecase

import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.domain.model.UserState
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import javax.inject.Inject

class SelectRoleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(role: UserRole): DataResult<UserState> =
        repository.selectRole(role)
}
