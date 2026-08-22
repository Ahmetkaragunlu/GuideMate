package com.ahmetkaragunlu.guidemate.common.pagination

data class PagedResult<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean,
)
