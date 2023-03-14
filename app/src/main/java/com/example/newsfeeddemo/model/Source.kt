package com.example.newsfeeddemo.model


import androidx.room.Entity
import com.example.newsfeeddemo.utils.Constant.ARTICLE_TABLE
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    @SerializedName("name")
    val name: String
)