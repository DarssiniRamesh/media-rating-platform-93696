package com.example.android_tv_frontend.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// PUBLIC_INTERFACE
interface LikesService {
    /** GET /likes/v1/like - Check existing like for a media group_id */
    @GET("/likes/v1/like")
    suspend fun getLike(@Query("group_id") groupId: Long): LikeResponse

    /** POST /likes/v1/like - Submit like for a media group_id */
    @POST("/likes/v1/like")
    suspend fun postLike(@Body body: LikePostBody): LikeResponse
}

// PUBLIC_INTERFACE
interface ContentService {
    /** GET /content/v1/data - Get content info including title and images */
    @GET("/content/v1/data")
    suspend fun getContent(@Query("group_id") groupId: Long): ContentResponse
}

// PUBLIC_INTERFACE
interface APAService {
    /** GET /apa/metadata - Retrieve APA metadata with legend keys and settings */
    @GET("/apa/metadata")
    suspend fun getMetadata(): APAMetadataResponse

    /** GET /apa/assets - Retrieve APA assets like icons */
    @GET("/apa/assets")
    suspend fun getAssets(): APAAssetsResponse
}
