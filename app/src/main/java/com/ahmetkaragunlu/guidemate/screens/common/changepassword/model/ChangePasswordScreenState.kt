package com.ahmetkaragunlu.guidemate.screens.common.changepassword.model

data class ChangePasswordScreenState(
    val errorMessage: String? = null,
    val currentPasswordErrorMessage: String? = null,
    val newPasswordErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val showSuccessDialog: Boolean = false,
)
