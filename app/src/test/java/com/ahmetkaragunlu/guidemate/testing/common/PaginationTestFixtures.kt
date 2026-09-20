package com.ahmetkaragunlu.guidemate.testing.common

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult

fun <T> emptyPage(): PagedResult<T> =
    PagedResult(
        items = emptyList(),
        page = 0,
        size = 20,
        totalElements = 0,
        totalPages = 0,
        isFirst = true,
        isLast = true,
    )
