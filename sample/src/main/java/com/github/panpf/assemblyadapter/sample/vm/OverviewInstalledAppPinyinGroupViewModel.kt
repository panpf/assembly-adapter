package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OverviewInstalledAppPinyinGroupViewModel(application: Application) :
    AndroidViewModel(application) {

    val pinyinGroupAppListData = MutableLiveData<List<Any>>()
    val loadingData = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            loadingData.postValue(true)
            pinyinGroupAppListData.postValue(insertOverview(groupByPinyin(loadInstalledAppList())))
            loadingData.postValue(false)
        }
    }

    private suspend fun loadInstalledAppList(): List<AppInfo> = withContext(Dispatchers.IO) {
        val appContext = getApplication<Application>()
        appContext.packageManager.getInstalledPackages(0).mapNotNull { packageInfo ->
            AppInfo.fromPackageInfo(appContext, packageInfo)
        }
    }

    private suspend fun groupByPinyin(appList: List<AppInfo>): List<AppGroup> =
        withContext(Dispatchers.IO) {
            appList
                .groupBy { it.namePinyin.first().uppercase() }
                .map { it -> AppGroup(it.key, it.value.sortedBy { it.namePinyinLowerCase }) }
                .sortedBy { it.title }
        }

    private suspend fun insertOverview(appGroupList: List<AppGroup>): List<Any> =
        withContext(Dispatchers.IO) {
            ArrayList<Any>().apply {
                add(AppsOverview.createAppsOverviewByAppGroup(appGroupList))
                addAll(appGroupList)
            }
        }
}