/*
 * Copyright (C) 2021 panpf <panpfpanpf@oulook.com>
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
package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.ListSeparator
import com.github.panpf.assemblyadapter.sample.ds.AppListPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PinyinFlatPagingAppListViewModel(application: Application) :
    AndroidViewModel(application) {

    /**
     * Contains the following class types: PinyinGroup、AppInfo
     */
    val pinyinFlatAppListDataFlow: Flow<PagingData<Any>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            initialKey = 0,
            pagingSourceFactory = AppListPagingSource.Factory(getApplication())
        ).flow.map {
            it.insertSeparators { before: AppInfo?, after: AppInfo? ->
                withContext(Dispatchers.IO) {
                    when {
                        before != null && after != null -> {
                            val beforeFirChar = before.namePinyinLowerCase.first().uppercaseChar()
                            val afterFirChar = after.namePinyinLowerCase.first().uppercaseChar()
                            if (beforeFirChar != afterFirChar) ListSeparator(afterFirChar.toString()) else null
                        }
                        before == null && after != null -> {
                            ListSeparator(after.namePinyinLowerCase.first().uppercase())
                        }
                        else -> null
                    }
                }
            }
        }
}