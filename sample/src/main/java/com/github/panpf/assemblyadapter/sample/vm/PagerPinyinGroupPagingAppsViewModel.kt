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
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.ds.PinyinGroupAppsPagerSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

class PagerPinyinGroupPagingAppsViewModel(application: Application) :
    AndroidViewModel(application) {

    /**
     * Contains the following class types: AppGroup
     */
    val pinyinFlatChunkedAppListDataFlow: Flow<PagingData<Any>> =
        Pager(
            PagingConfig(5, 1, false, 5),
            0,
            PinyinGroupAppsPagerSource.Factory(getApplication())
        ).flow

    @OptIn(ExperimentalCoroutinesApi::class)
    val appsOverviewData: LiveData<AppsOverview> = channelFlow {
        withContext(Dispatchers.IO) {
            val appsOverview = AppsOverview.build(getApplication())
            // AppsOverview 数据延迟发放松是为了测试主体数据 pinyinFlatChunkedAppListDataFlow 显示出来以后
            // 再在列表头部以刷新 FragmentStateAdapter 的方式显示 AppsOverview 时 ViewPager2 能否正确刷新
            Thread.sleep(2000)
            send(appsOverview)
        }
    }.asLiveData()
}