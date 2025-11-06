package com.example.android_tv_frontend.data.api

import com.google.gson.annotations.SerializedName
import java.time.Instant

// Data models representing API responses and domain entities

data class LikeResponse(
    @SerializedName("data") val data: LikeData
)

data class LikeData(
    @SerializedName("like") val like: Int,
    @SerializedName("stars") val stars: Int?,
    @SerializedName("time") val time: String,
    @SerializedName("group_id") val groupId: Long,
    @SerializedName("group_uid") val groupUid: String?
)

data class LikePostBody(
    @SerializedName("group_id") val groupId: Long,
    @SerializedName("like") val like: Int,
    @SerializedName("stars") val stars: Int?,
    @SerializedName("time") val time: String
)

data class ContentResponse(
    @SerializedName("response") val response: ContentGroupWrapper
)

data class ContentGroupWrapper(
    @SerializedName("group") val group: ContentGroup
)

data class ContentGroup(
    @SerializedName("common") val common: ContentCommon,
    @SerializedName("image_clean_horizontal") val poster: String?,
    @SerializedName("image_background") val background: String?
)

data class ContentCommon(
    @SerializedName("title") val title: String
)

data class APAMetadataResponse(
    @SerializedName("vod_rating_settings") val settings: VodRatingSettings,
    @SerializedName("legend_keys") val legendKeys: Map<String, String> = emptyMap()
)

data class APAAssetsResponse(
    @SerializedName("icons") val icons: Map<String, String> = emptyMap()
)

data class VodRatingSettings(
    @SerializedName("display_time") val displayTime: Int = 30,
    @SerializedName("max_display_time") val maxDisplayTime: Int = 60,
    @SerializedName("rollingcreditstime") val rollingCreditsTime: Int = 20
)

// Domain models used by UI

data class MediaItem(
    val groupId: Long,
    val title: String,
    val posterUrl: String,
    val backgroundUrl: String,
    val avgRating: Float = 0f,
    val ratingsCount: Int = 0
)

data class Rating(
    val groupId: Long,
    val like: Int, // -1, 1, 2 or 0
    val stars: Int = 0,
    val time: Instant = Instant.now()
)
