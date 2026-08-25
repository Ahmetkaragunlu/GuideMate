package com.ahmetkaragunlu.guidemate.auth.domain.session

interface AccessTokenProvider {
    fun getAccessToken(): String?
}
