package com.example.newsfeeddemo.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsfeeddemo.model.Article
import com.example.newsfeeddemo.utils.Constant.ARTICLE_TABLE

@Dao
interface ArticleDao {
    @Query("SELECT * FROM $ARTICLE_TABLE")
    fun getAllArticles(): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewsArticles(news: List<Article>)

    @Query("DELETE FROM $ARTICLE_TABLE")
    suspend fun deleteAllArticles()
}