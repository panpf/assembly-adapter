package com.github.panpf.assemblyadapter.sample.ds

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.utils.AppListHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppListPagerSource private constructor(
    private val context: Context, private val factory: Factory
) : PagingSource<Int, AppInfo>() {

    class Factory(val context: Context) : Function0<AppListPagerSource> {

        var appListHelper: AppListHelper? = null

        override fun invoke(): AppListPagerSource {
            return AppListPagerSource(context, this)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AppInfo>): Int = 0

    /**
     * Contains the following class types: PinyinGroup、AppInfo
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppInfo> {
        if (params is LoadParams.Refresh) {
            factory.appListHelper = null
        } else {
            withContext(Dispatchers.IO) {
                Thread.sleep(1500)
            }
        }
        val appListHelper = factory.appListHelper ?: withContext(Dispatchers.IO) {
            AppListHelper(context)
        }.apply {
            factory.appListHelper = this
        }
        val startIndex = params.key!!
        val endIndexExclusive =
            (startIndex + params.loadSize).coerceAtMost(appListHelper.count)
        val result = appListHelper.getRange(startIndex, endIndexExclusive)
        val nextKey =
            if (result.isNotEmpty() && endIndexExclusive < appListHelper.count) endIndexExclusive else null
        return LoadResult.Page(result, null, nextKey)
    }
}