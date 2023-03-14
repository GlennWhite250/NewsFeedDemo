package com.example.newsfeeddemo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsfeeddemo.model.ArticleRemoteKeys
import com.example.newsfeeddemo.utils.Constant.ARTICLE_REMOTE_KEYS_TABLE

@Dao
interface ArticleRemoteKeysDao {
    @Query("SELECT * FROM $ARTICLE_REMOTE_KEYS_TABLE WHERE id = :id")
    suspend fun getRemoteKeys(id: Int): ArticleRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<ArticleRemoteKeys>)

    @Query("DELETE FROM $ARTICLE_REMOTE_KEYS_TABLE")
    suspend fun deleteAllRemoteKeys()
}