package com.example.newsfeeddemo.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsfeeddemo.BuildConfig
import com.example.newsfeeddemo.data.local.ArticleDatabase
import com.example.newsfeeddemo.data.remote.ArticlesApi
import com.example.newsfeeddemo.model.Article
import com.example.newsfeeddemo.model.ArticleRemoteKeys
import com.example.newsfeeddemo.utils.Constant.COUNTRY_CODE
import com.example.newsfeeddemo.utils.Constant.ITEMS_PER_PAGE

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val articlesApi: ArticlesApi,
    private val articleDatabase: ArticleDatabase
): RemoteMediator<Int, Article>() {

    private val articleDao = articleDatabase.ArticleDao()
    private val articleRemoteKeysDao = articleDatabase.ArticleRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        return try {
            val currentPage = when(loadType){
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosetToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = articlesApi.getAllArticles(
                page = currentPage,
                pageSize = ITEMS_PER_PAGE,
                country = COUNTRY_CODE,
                apiKey = BuildConfig.API_KEY
            )
            val endOfPagnationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPagnationReached) null else currentPage + 1

            articleDatabase.withTransaction {
                if (loadType == LoadType.REFRESH){
                    articleDao.deleteAllArticles()
                    articleRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.map {
                    ArticleRemoteKeys(
                        id = it.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                articleRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                articleDao.addNewsArticles(news = response)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPagnationReached)
        } catch (e: Exception){
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosetToCurrentPosition(
        state: PagingState<Int, Article>
    ): ArticleRemoteKeys? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let {
                articleRemoteKeysDao.getRemoteKeys(id = it)
            }
        }
    }

    private suspend fun getRemoteKeyFirstItem(
        state: PagingState<Int, Article>
    ): ArticleRemoteKeys?{
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let {
            articleRemoteKeysDao.getRemoteKeys(id = it.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Article>
    ): ArticleRemoteKeys?{
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            articleRemoteKeysDao.getRemoteKeys(id = it.id)
        }
    }
}