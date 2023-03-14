package com.example.newsfeeddemo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsfeeddemo.utils.Constant.ARTICLE_REMOTE_KEYS_TABLE

@Entity(tableName = ARTICLE_REMOTE_KEYS_TABLE)
data class ArticleRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?
)