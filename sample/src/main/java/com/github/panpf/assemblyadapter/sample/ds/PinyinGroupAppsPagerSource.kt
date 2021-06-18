package com.github.panpf.assemblyadapter.sample.ds

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.panpf.assemblyadapter.sample.utils.PinyinGroupAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PinyinGroupAppsPagerSource private constructor(
    private val context: Context, private val factory: Factory
) : PagingSource<Int, Any>() {

    class Factory(val context: Context) : Function0<PinyinGroupAppsPagerSource> {

        var pinyinGroupAppsHelper: PinyinGroupAppsHelper? = null

        override fun invoke(): PinyinGroupAppsPagerSource {
            return PinyinGroupAppsPagerSource(context, this)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        if (params is LoadParams.Refresh) {
            factory.pinyinGroupAppsHelper = null
        } else {
            withContext(Dispatchers.IO) {
                Thread.sleep(1500)
            }
        }
        val pinyinFlatAppsHelper =
            factory.pinyinGroupAppsHelper ?: withContext(Dispatchers.IO) {
                PinyinGroupAppsHelper(context)
            }.apply {
                factory.pinyinGroupAppsHelper = this
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