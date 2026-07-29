package com.ahmetkaragunlu.guidemate.screens.auth.roleselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.common.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.toMessage
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.domain.usecase.SelectRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RoleSelectionViewModel @Inject constructor(
    private val selectRoleUseCase: SelectRoleUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val _screenState = MutableStateFlow(RoleSelectionScreenState())
    val screenState: StateFlow<RoleSelectionScreenState> = _screenState.asStateFlow()

    fun selectRole(role: UserRole) {
        if (_screenState.value.isLoading) return
        _screenState.update { it.copy(selectedRole = role) }
    }

    fun confirmRoleSelection() {
        val role = _screenState.value.selectedRole
        if (role == null) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_select_role))
            }
            return
        }
        if (_screenState.value.isLoading) return

        viewModelScope.launch {
            _screenState.update { it.copy(isLoading = true) }
            when (val result = selectRoleUseCase(role)) {
                is DataResult.Success -> {
                    _screenState.update { it.copy(isLoading = false) }
                }

                is DataResult.Error -> {
                    _screenState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.toMessage(resourceProvider),
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }
}
