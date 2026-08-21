package com.ahmetkaragunlu.guidemate.auth.presentation.roleselection

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole

data class RoleSelectionScreenState(
    val selectedRole: UserRole? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
