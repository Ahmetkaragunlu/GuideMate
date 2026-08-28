package com.ahmetkaragunlu.guidemate.auth.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

@Singleton
class AuthPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val preferences =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    suspend fun restoreUser(): UserState =
        preferences.first().toUserState().also { user ->
            _userState.value = user
        }

    suspend fun saveUser(user: UserState) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.userId ?: NO_USER_ID
            preferences.putOrRemove(EMAIL, user.email)
            preferences.putOrRemove(FIRST_NAME, user.firstName)
            preferences.putOrRemove(LAST_NAME, user.lastName)
            preferences[ROLE_SELECTED] = user.isRoleSelected
            preferences.putOrRemove(USER_ROLE, user.role?.toStoredValue())
            preferences.putOrRemove(AVATAR_MEDIA_ID, user.avatarMediaId)
            preferences.putOrRemove(AVATAR_URL, user.avatarUrl)
        }
        _userState.value = user
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(EMAIL)
            preferences.remove(FIRST_NAME)
            preferences.remove(LAST_NAME)
            preferences.remove(ROLE_SELECTED)
            preferences.remove(USER_ROLE)
            preferences.remove(AVATAR_MEDIA_ID)
            preferences.remove(AVATAR_URL)
        }
        _userState.value = UserState()
    }

    suspend fun isOnboardingCompleted(): Boolean =
        preferences.first()[ONBOARDING_COMPLETED] ?: false

    suspend fun completeOnboarding() {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = true
        }
    }

    private fun Preferences.toUserState(): UserState =
        UserState(
            userId = this[USER_ID]?.takeUnless { it == NO_USER_ID },
            email = this[EMAIL],
            firstName = this[FIRST_NAME],
            lastName = this[LAST_NAME],
            isRoleSelected = this[ROLE_SELECTED] ?: false,
            role = this[USER_ROLE]?.toUserRoleOrNull(),
            avatarMediaId = this[AVATAR_MEDIA_ID],
            avatarUrl = this[AVATAR_URL],
        )

    private fun String.toUserRoleOrNull(): UserRole? =
        when (this) {
            ROLE_TOURIST,
            UserRole.TOURIST.name,
            -> UserRole.TOURIST

            ROLE_GUIDE,
            UserRole.GUIDE.name,
            -> UserRole.GUIDE

            else -> null
        }

    private fun UserRole.toStoredValue(): String =
        when (this) {
            UserRole.TOURIST -> ROLE_TOURIST
            UserRole.GUIDE -> ROLE_GUIDE
        }

    private fun MutablePreferences.putOrRemove(
        key: Preferences.Key<String>,
        value: String?,
    ) {
        if (value == null) {
            remove(key)
        } else {
            this[key] = value
        }
    }

    private companion object {
        val USER_ID = longPreferencesKey("user_id")
        val EMAIL = stringPreferencesKey("email")
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val ROLE_SELECTED = booleanPreferencesKey("role_selected")
        val USER_ROLE = stringPreferencesKey("user_role")
        val AVATAR_MEDIA_ID = stringPreferencesKey("avatar_media_id")
        val AVATAR_URL = stringPreferencesKey("avatar_url")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

        const val NO_USER_ID = -1L
        const val ROLE_TOURIST = "ROLE_TOURIST"
        const val ROLE_GUIDE = "ROLE_GUIDE"
    }
}
