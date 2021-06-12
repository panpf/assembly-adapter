package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseInstalledAppPinyinGroupViewModel(application: Application) :
    BaseInstalledAppViewModel(application) {

    val pinyinGroupAppListData = MutableLiveData<List<AppGroup>>()
    val loadingData = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            loadingData.postValue(true)
            pinyinGroupAppListData.postValue(groupByPinyin(loadInstalledAppList()))
            loadingData.postValue(false)
        }
    }

    private suspend fun groupByPinyin(appList: List<AppInfo>): List<AppGroup> =
        withContext(Dispatchers.IO) {
            appList
                .groupBy { it.namePinyin.first().uppercase() }
                .map { AppGroup(it.key, it.value) }
                .sortedBy { it.title }
        }
}