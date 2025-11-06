package com.example.android_tv_frontend.data.api

import android.util.Log
import com.example.android_tv_frontend.BuildConfig
import com.example.android_tv_frontend.data.mock.MockAPAService
import com.example.android_tv_frontend.data.mock.MockContentService
import com.example.android_tv_frontend.data.mock.MockLikesService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private fun okHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
            else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    private fun retrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // PUBLIC_INTERFACE
    fun likesService(): LikesService {
        return if (BuildConfig.USE_MOCK_API) {
            Log.d("ApiClient", "Using MockLikesService")
            MockLikesService()
        } else {
            retrofit(BuildConfig.API_BASE_URL).create(LikesService::class.java)
        }
    }

    // PUBLIC_INTERFACE
    fun contentService(): ContentService {
        return if (BuildConfig.USE_MOCK_API) {
            MockContentService()
        } else {
            retrofit(BuildConfig.API_BASE_URL).create(ContentService::class.java)
        }
    }

    // PUBLIC_INTERFACE
    fun apaService(): APAService {
        return if (BuildConfig.USE_MOCK_API) {
            MockAPAService()
        } else {
            retrofit(BuildConfig.API_BASE_URL).create(APAService::class.java)
        }
    }
}
