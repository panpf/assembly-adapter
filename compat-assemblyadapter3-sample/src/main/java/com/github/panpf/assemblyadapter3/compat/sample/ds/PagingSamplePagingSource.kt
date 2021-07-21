package com.github.panpf.assemblyadapter3.compat.sample.ds

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.github.panpf.assemblyadapter3.compat.sample.util.PinyinFlatAppsHelper
import com.github.panpf.assemblyadapter3.compat.sample.util.minExecuteTime

class PagingSamplePagingSource(application: Application) : PagingSource<Int, Any>() {

    private val appHelper by lazy { PinyinFlatAppsHelper(application) }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        val startIndex = params.key ?: 0
        val result = withContext(Dispatchers.IO) {
            minExecuteTime(1500) {
                appHelper.getRange(startIndex, startIndex + params.loadSize)
            }
        }
        val nextKey = if (result.size >= params.loadSize) startIndex + params.loadSize else null
        return LoadResult.Page(result, null, nextKey)
    }
}