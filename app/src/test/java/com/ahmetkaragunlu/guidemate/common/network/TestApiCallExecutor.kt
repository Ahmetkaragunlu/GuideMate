package com.ahmetkaragunlu.guidemate.common.network

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.google.gson.Gson

fun testApiCallExecutor(): ApiCallExecutor =
    ApiCallExecutor(
        apiErrorParser = ApiErrorParser(Gson()),
        networkExceptionMapper = NetworkExceptionMapper(),
    )
