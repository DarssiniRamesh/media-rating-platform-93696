package com.example.android_tv_frontend.data.mock

import com.example.android_tv_frontend.data.api.*
import kotlinx.coroutines.delay
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

private val likesStore = ConcurrentHashMap<Long, LikeData>()

class MockLikesService : LikesService {
    override suspend fun getLike(groupId: Long): LikeResponse {
        delay(100)
        val data = likesStore[groupId] ?: LikeData(
            like = 0, stars = 0, time = Instant.now().toString(),
            groupId = groupId, groupUid = null
        )
        return LikeResponse(data)
    }

    override suspend fun postLike(body: LikePostBody): LikeResponse {
        delay(150)
        val data = LikeData(
            like = body.like,
            stars = body.stars ?: 0,
            time = body.time,
            groupId = body.groupId,
            groupUid = null
        )
        likesStore[body.groupId] = data
        return LikeResponse(data)
    }
}

class MockContentService : ContentService {
    override suspend fun getContent(groupId: Long): ContentResponse {
        delay(80)
        val title = "Sample Title #$groupId"
        val poster = "https://picsum.photos/seed/${groupId}/600/338"
        val bg = "https://picsum.photos/seed/${groupId}b/1280/720"
        return ContentResponse(
            ContentGroupWrapper(
                ContentGroup(
                    common = ContentCommon(title),
                    poster = poster,
                    background = bg
                )
            )
        )
    }
}

class MockAPAService : APAService {
    override suspend fun getMetadata(): APAMetadataResponse {
        delay(50)
        return APAMetadataResponse(
            settings = VodRatingSettings(displayTime = 30, maxDisplayTime = 60, rollingCreditsTime = 20),
            legendKeys = mapOf(
                "rating_title_key" to "rating_title",
                "rating_message_key" to "rating_message",
                "like_text_key" to "rating_like",
                "love_text_key" to "rating_love",
                "dislike_text_key" to "rating_dislike",
                "close_text_key" to "rating_close",
                "countdown_text_key" to "rating_countdown"
            )
        )
    }

    override suspend fun getAssets(): APAAssetsResponse {
        delay(50)
        return APAAssetsResponse(
            icons = mapOf(
                "like" to "https://img.icons8.com/ios-filled/100/2563EB/thumb-up.png",
                "love" to "https://img.icons8.com/ios-filled/100/F59E0B/hearts.png",
                "dislike" to "https://img.icons8.com/ios-filled/100/2563EB/thumbs-down.png",
                "close" to "https://img.icons8.com/ios-filled/100/FFFFFF/delete-sign.png"
            )
        )
    }
}
