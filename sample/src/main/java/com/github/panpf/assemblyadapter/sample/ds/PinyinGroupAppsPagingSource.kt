/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter.sample.ds

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.panpf.assemblyadapter.sample.util.PinyinGroupAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PinyinGroupAppsPagingSource private constructor(
    private val context: Context, private val factory: Factory
) : PagingSource<Int, Any>() {

    class Factory(val context: Context) : Function0<PinyinGroupAppsPagingSource> {

        var pinyinGroupAppsHelper: PinyinGroupAppsHelper? = null

        override fun invoke(): PinyinGroupAppsPagingSource {
            return PinyinGroupAppsPagingSource(context, this)
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