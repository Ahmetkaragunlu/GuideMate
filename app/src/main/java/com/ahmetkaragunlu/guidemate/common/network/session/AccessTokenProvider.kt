package com.ahmetkaragunlu.guidemate.common.network.session

interface AccessTokenProvider {
    fun getAccessToken(): String?
}
