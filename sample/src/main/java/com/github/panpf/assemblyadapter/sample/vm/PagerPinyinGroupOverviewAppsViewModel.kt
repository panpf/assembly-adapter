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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.util.PinyinGroupAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PagerPinyinGroupOverviewAppsViewModel(application: Application) :
    AndroidViewModel(application) {

    val loadingData = MutableLiveData<Boolean>()
    val pinyinGroupAppListData = MutableLiveData<List<AppGroup>>()
    val appsOverviewData = MutableLiveData<AppsOverview>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadingData.postValue(true)

            val appsOverview = AppsOverview.build(getApplication())
            val appGroupList = PinyinGroupAppsHelper(getApplication()).getAll()
            pinyinGroupAppListData.postValue(appGroupList)
            /*
             * AppsOverview 数据延迟发送是为了测试主体数据 appGroupList 显示出来以后
             * 再用刷新 PagerAdapter 的方式在列表头部显示 AppsOverview 时 ViewPager 能否正确刷新
             */
            Thread.sleep(1500)
            appsOverviewData.postValue(appsOverview)

            loadingData.postValue(false)
        }
    }
}