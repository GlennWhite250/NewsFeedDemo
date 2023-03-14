package com.example.newsfeeddemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsfeeddemo.data.local.dao.ArticleDao
import com.example.newsfeeddemo.data.local.dao.ArticleRemoteKeysDao
import com.example.newsfeeddemo.model.Article
import com.example.newsfeeddemo.model.ArticleRemoteKeys

@Database(entities = [Article::class, ArticleRemoteKeys::class], version = 1)
abstract class ArticleDatabase: RoomDatabase() {
    abstract fun ArticleDao(): ArticleDao
    abstract fun ArticleRemoteKeysDao(): ArticleRemoteKeysDao
}