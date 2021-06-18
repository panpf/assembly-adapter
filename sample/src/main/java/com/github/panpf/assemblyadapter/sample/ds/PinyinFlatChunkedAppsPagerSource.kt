package com.github.panpf.assemblyadapter.sample.ds

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.panpf.assemblyadapter.sample.utils.PinyinFlatChunkedAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Contains the following class types: PinyinGroup、Apps
 */
class PinyinFlatChunkedAppsPagerSource private constructor(
    private val context: Context, private val factory: Factory
) : PagingSource<Int, Any>() {

    class Factory(val context: Context) : Function0<PinyinFlatChunkedAppsPagerSource> {

        var pinyinFlatChunkedAppsHelper: PinyinFlatChunkedAppsHelper? = null

        override fun invoke(): PinyinFlatChunkedAppsPagerSource {
            return PinyinFlatChunkedAppsPagerSource(context, this)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        return 0
    }

    /**
     * Contains the following class types: PinyinGroup、Apps
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        if (params is LoadParams.Refresh) {
            factory.pinyinFlatChunkedAppsHelper = null
        } else {
            withContext(Dispatchers.IO) {
                Thread.sleep(1500)
            }
        }
        val pinyinFlatAppsHelper =
            factory.pinyinFlatChunkedAppsHelper ?: withContext(Dispatchers.IO) {
                PinyinFlatChunkedAppsHelper(context)
            }.apply {
                factory.pinyinFlatChunkedAppsHelper = this
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