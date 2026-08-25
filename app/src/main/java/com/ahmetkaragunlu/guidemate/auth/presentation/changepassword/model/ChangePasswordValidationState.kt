package com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.model

data class ChangePasswordValidationState(
    val isCurrentPasswordValid: Boolean,
    val isNewPasswordValid: Boolean,
    val isConfirmationValid: Boolean,
)
