package com.github.panpf.assemblyadapter.sample.ds

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.panpf.assemblyadapter.sample.utils.PinyinFlatAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PinyinFlatAppsPagerSource private constructor(
    private val context: Context, private val factory: Factory
) : PagingSource<Int, Any>() {

    class Factory(val context: Context) : Function0<PinyinFlatAppsPagerSource> {

        var pinyinFlatAppsHelper: PinyinFlatAppsHelper? = null

        override fun invoke(): PinyinFlatAppsPagerSource {
            return PinyinFlatAppsPagerSource(context, this)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        return 0
    }

    /**
     * Contains the following class types: PinyinGroup、AppInfo
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        if (params is LoadParams.Refresh) {
            factory.pinyinFlatAppsHelper = null
        } else {
            withContext(Dispatchers.IO) {
                Thread.sleep(1500)
            }
        }
        val pinyinFlatAppsHelper = factory.pinyinFlatAppsHelper ?: withContext(Dispatchers.IO) {
            PinyinFlatAppsHelper(context)
        }.apply {
            factory.pinyinFlatAppsHelper = this
        }
        val startIndex = params.key!!
        val endIndexExclusive =
            (startIndex + params.loadSize).coerceAtMost(pinyinFlatAppsHelper.count)
        val result = pinyinFlatAppsHelper.getRange(startIndex, endIndexExclusive)
        val nextKey =
            if (result.isNotEmpty() && endIndexExclusive < pinyinFlatAppsHelper.count) endIndexExclusive else null
        return LoadResult.Page(result, null, nextKey)
    }
}