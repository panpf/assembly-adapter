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
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.util.AppListHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppListPagingSource private constructor(
    private val context: Context, private val factory: Factory
) : PagingSource<Int, AppInfo>() {

    class Factory(val context: Context) : Function0<AppListPagingSource> {

        var appListHelper: AppListHelper? = null

        override fun invoke(): AppListPagingSource {
            return AppListPagingSource(context, this)
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