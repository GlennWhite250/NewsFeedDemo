package com.example.newsfeeddemo.data.remote

import com.example.newsfeeddemo.model.Article
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesApi {
    @GET("v2/top-headlines")
    suspend fun getAllArticles(
        @Query("apiKey")
        apiKey: String,
        @Query("pageSize")
        pageSize: Int,
        @Query("country")
        country: String,
        @Query("page")
        page: Int
    ): List<Article>
}