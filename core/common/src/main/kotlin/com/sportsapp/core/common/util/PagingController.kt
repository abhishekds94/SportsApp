package com.sportsapp.core.common.util

class PagingController<T>(
    private val initialSize: Int,
    private val pageSize: Int
) {
    private var allItems: List<T> = emptyList()
    private var currentSize: Int = 0

    fun reset(items: List<T>): Page<T> {
        allItems = items
        currentSize = minOf(initialSize, allItems.size)
        return currentPage()
    }

    fun loadMore(): Page<T> {
        if (allItems.isEmpty()) return currentPage()
        currentSize = minOf(currentSize + pageSize, allItems.size)
        return currentPage()
    }

    fun currentPage(): Page<T> {
        val shown = allItems.take(currentSize)
        return Page(
            all = allItems,
            shown = shown,
            hasMore = shown.size < allItems.size
        )
    }

    data class Page<T>(
        val all: List<T>,
        val shown: List<T>,
        val hasMore: Boolean
    )
}
