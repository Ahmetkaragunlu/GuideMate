package com.ahmetkaragunlu.guidemate.profile.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.domain.repository.UserAvatarRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.tourist.model.ProfileUiState
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TouristProfileViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val walletRepository: WalletRepository,
        private val userAvatarRepository: UserAvatarRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow(ProfileUiState())
        val uiState: StateFlow<ProfileUiState> = mutableUiState.asStateFlow()

        init {
            viewModelScope.launch {
                userRepository.userState.collect { user ->
                    mutableUiState.update {
                        it.copy(
                            fullName =
                                listOfNotNull(user.firstName, user.lastName).joinToString(" "),
                            email = user.email.orEmpty(),
                            avatarUrl = user.avatarUrl,
                        )
                    }
                }
            }
            refresh()
        }

        fun refresh() {
            viewModelScope.launch {
                mutableUiState.update { it.copy(loadState = ContentLoadState.LOADING) }
                when (val result = walletRepository.getWallet()) {
                    is DataResult.Success ->
                        mutableUiState.update {
                            it.copy(
                                loadState = ContentLoadState.CONTENT,
                                balanceMinor = result.data.balanceMinor,
                                currencyCode = result.data.currencyCode,
                            )
                        }
                    is DataResult.Error ->
                        mutableUiState.update { it.copy(loadState = ContentLoadState.ERROR) }
                }
            }
        }

        fun onProfileImageSelected(uri: String) {
            if (mutableUiState.value.isAvatarUpdating) return
            mutableUiState.update {
                it.copy(selectedAvatarUri = uri, isAvatarUpdating = true)
            }
            viewModelScope.launch {
                when (val result = userAvatarRepository.updateAvatar(uri)) {
                    is DataResult.Success ->
                        mutableUiState.update {
                            it.copy(
                                avatarUrl = result.data.imageUrl,
                                userMessage = resourceProvider.getString(R.string.profile_photo_update_success),
                            )
                        }
                    is DataResult.Error ->
                        mutableUiState.update {
                            it.copy(userMessage = result.error.toMessage(resourceProvider))
                        }
                }
                mutableUiState.update {
                    it.copy(selectedAvatarUri = null, isAvatarUpdating = false)
                }
            }
        }

        fun onUserMessageShown() {
            mutableUiState.update { it.copy(userMessage = null) }
        }
    }
