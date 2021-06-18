package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InstalledAppPinyinFlatViewModel(application: Application) : AndroidViewModel(application) {

    val pinyinFlatAppListData = MutableLiveData<List<Any>>()
    val loadingData = MutableLiveData<Boolean>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val appsOverviewData = channelFlow {
        withContext(Dispatchers.IO) {
            send(AppsOverview.build(getApplication()))
        }
    }.asLiveData()

    init {
        viewModelScope.launch {
            loadingData.postValue(true)
            pinyinFlatAppListData.postValue(flatByPinyin(loadInstalledAppList()))
            loadingData.postValue(false)
        }
    }

    private suspend fun loadInstalledAppList(): List<AppInfo> = withContext(Dispatchers.IO) {
        val appContext = getApplication<Application>()
        appContext.packageManager.getInstalledPackages(0).mapNotNull { packageInfo ->
            AppInfo.fromPackageInfo(appContext, packageInfo)
        }
    }

    private suspend fun flatByPinyin(appList: List<AppInfo>): List<Any> =
        withContext(Dispatchers.IO) {
            val sortedAppList = appList.sortedBy { it.namePinyinLowerCase }
            ArrayList<Any>().apply {
                var lastPinyinFirstChar: Char? = null
                sortedAppList.forEach { app ->
                    val namePinyinFirstChar = app.namePinyin.first().uppercaseChar()
                    if (lastPinyinFirstChar == null || namePinyinFirstChar != lastPinyinFirstChar) {
                        add(PinyinGroup(namePinyinFirstChar.toString()))
                        lastPinyinFirstChar = namePinyinFirstChar
                    }
                    add(app)
                }
            }
        }
}