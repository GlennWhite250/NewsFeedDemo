package com.example.newsfeeddemo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsfeeddemo.data.local.ArticleDatabase
import com.example.newsfeeddemo.data.paging.ArticleRemoteMediator
import com.example.newsfeeddemo.data.remote.ArticlesApi
import com.example.newsfeeddemo.model.Article
import com.example.newsfeeddemo.utils.Constant.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class Repository @Inject constructor(
    private val articlesApi: ArticlesApi,
    private val articleDatabase: ArticleDatabase
) {
    fun getAllArticles(): Flow<PagingData<Article>> {
        val pagingSourceFactory = {
            articleDatabase.ArticleDao().getAllArticles()
        }
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = ArticleRemoteMediator(
                articlesApi = articlesApi,
                articleDatabase = articleDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}