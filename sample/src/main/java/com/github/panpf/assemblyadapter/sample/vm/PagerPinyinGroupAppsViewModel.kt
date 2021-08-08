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
import com.github.panpf.assemblyadapter.sample.util.PinyinGroupAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PagerPinyinGroupAppsViewModel(application: Application) : AndroidViewModel(application) {

    val loadingData = MutableLiveData<Boolean>()
    val pinyinGroupAppListData = MutableLiveData<List<AppGroup>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadingData.postValue(true)

            val appGroupList = PinyinGroupAppsHelper(getApplication()).getAll()
            pinyinGroupAppListData.postValue(appGroupList)

            loadingData.postValue(false)
        }
    }
}