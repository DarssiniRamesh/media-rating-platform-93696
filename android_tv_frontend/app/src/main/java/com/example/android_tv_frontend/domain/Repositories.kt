package com.example.android_tv_frontend.domain

import com.example.android_tv_frontend.data.api.*
import com.example.android_tv_frontend.data.api.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max
import kotlin.math.min

// In-memory aggregation store for demo
private val ratingAgg: ConcurrentHashMap<Long, Pair<Int, Int>> = ConcurrentHashMap()

// PUBLIC_INTERFACE
class RatingsRepository(
    private val likesService: LikesService
) {
    suspend fun getPriorLike(groupId: Long): Int = withContext(Dispatchers.IO) {
        runCatching { likesService.getLike(groupId).data.like }.getOrDefault(0)
    }

    suspend fun submitLike(groupId: Long, like: Int, stars: Int? = null): LikeData =
        withContext(Dispatchers.IO) {
            val body = LikePostBody(
                groupId = groupId,
                like = like,
                stars = stars,
                time = Instant.now().toString()
            )
            val resp = likesService.postLike(body).data
            // Update simple aggregation (avg stars based on like mapping)
            val current = ratingAgg[groupId] ?: 0 to 0
            val inc = when (like) {
                2 -> 5 // love -> 5 stars
                1 -> 4
                -1 -> 2
                else -> 0
            }
            val newSum = current.first + inc
            val newCount = current.second + 1
            ratingAgg[groupId] = newSum to newCount
            resp
        }

    // PUBLIC_INTERFACE
    fun getAggregate(groupId: Long): Pair<Float, Int> {
        val (sum, count) = ratingAgg[groupId] ?: 0 to 0
        return if (count == 0) 0f to 0 else (sum.toFloat() / count.toFloat()) to count
    }
}

// PUBLIC_INTERFACE
class ContentRepository(
    private val contentService: ContentService
) {
    suspend fun getItem(groupId: Long): MediaItem = withContext(Dispatchers.IO) {
        val content = contentService.getContent(groupId).response.group
        val poster = content.poster ?: content.background ?: ""
        val bg = content.background ?: content.poster ?: ""
        MediaItem(
            groupId = groupId,
            title = content.common.title,
            posterUrl = poster,
            backgroundUrl = bg
        )
    }
}

// PUBLIC_INTERFACE
class APARepository(
    private val apaService: APAService
) {
    suspend fun getSettings(): VodRatingSettings = withContext(Dispatchers.IO) {
        apaService.getMetadata().settings
    }

    suspend fun getLegendKeys(): Map<String, String> = withContext(Dispatchers.IO) {
        apaService.getMetadata().legendKeys
    }

    suspend fun getAssets(): Map<String, String> = withContext(Dispatchers.IO) {
        apaService.getAssets().icons
    }
}
