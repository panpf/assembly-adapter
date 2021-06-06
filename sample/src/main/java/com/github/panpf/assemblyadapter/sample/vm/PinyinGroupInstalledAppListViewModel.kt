package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PinyinGroupInstalledAppListViewModel(application: Application) :
    AndroidViewModel(application) {

    val pinyinGroupInstalledAppListData = MutableLiveData<List<Any>>()
    val loadingData = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            loadingData.postValue(true)
            pinyinGroupInstalledAppListData.postValue(loadInstalledAppList())
            loadingData.postValue(false)
        }
    }

    private suspend fun loadInstalledAppList(): List<Any> = withContext(Dispatchers.IO) {
        val appContext = getApplication<Application>()
        val packageManager = appContext.packageManager
        insertPinyinGroup(packageManager.getInstalledPackages(0).map { packageInfo ->
            val name = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            AppInfo(
                packageName = packageInfo.packageName,
                name = name,
                namePinyin = Pinyin.toPinyin(name, null),
                versionName = packageInfo.versionName.orEmpty(),
                versionCode = packageInfo.versionCode,
                apkFilePath = packageInfo.applicationInfo.publicSourceDir,
                apkSize = File(packageInfo.applicationInfo.publicSourceDir).length(),
                packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0,
            )
        })
    }

    private fun insertPinyinGroup(appList: List<AppInfo>): List<Any> {
        val sortedAppList = appList.sortedBy { it.namePinyin }
        val pinyinGroupAppList = ArrayList<Any>()
        var lastPinyinFirstChar: Char? = null
        sortedAppList.forEach { app ->
            val namePinyinFirstChar = app.namePinyin.first()
            if (lastPinyinFirstChar == null || namePinyinFirstChar != lastPinyinFirstChar) {
                val namePinyinFirstCharUppercase = namePinyinFirstChar.uppercaseChar()
                pinyinGroupAppList.add(PinyinGroup(namePinyinFirstCharUppercase.toString()))
                lastPinyinFirstChar = namePinyinFirstChar
            }
            pinyinGroupAppList.add(app)
        }
        return pinyinGroupAppList
    }
}