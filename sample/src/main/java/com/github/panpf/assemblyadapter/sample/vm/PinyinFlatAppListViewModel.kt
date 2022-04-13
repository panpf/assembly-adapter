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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.base.LifecycleAndroidViewModel
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.util.PinyinFlatAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinyinFlatAppListViewModel(application: Application) :
    LifecycleAndroidViewModel(application) {

    val pinyinFlatAppListData = MutableLiveData<List<Any>>()
    val loadingData = MutableLiveData<Boolean>()

    init {
        refresh()
        monitorAppChanged()
    }

    fun refresh() {
        viewModelScope.launch {
            loadingData.postValue(true)
            val list = withContext(Dispatchers.IO) {
                val appList = PinyinFlatAppsHelper(getApplication()).getAll()
                // Large amounts of data are used to test the divider's performance
                ArrayList<Any>().apply {
                    repeat(30) {
                        addAll(appList)
                    }
                }
            }
            pinyinFlatAppListData.postValue(list)
            loadingData.postValue(false)
        }
    }

    private fun monitorAppChanged() {
        val packageIntentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
        }
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                refresh()
            }
        }
        application1.registerReceiver(receiver, packageIntentFilter)
        addOnClearedListener {
            application1.unregisterReceiver(receiver)
        }
    }
}