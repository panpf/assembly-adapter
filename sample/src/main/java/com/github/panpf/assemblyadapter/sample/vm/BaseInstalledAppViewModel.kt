package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.AndroidViewModel
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

open class BaseInstalledAppViewModel(application: Application) :
    AndroidViewModel(application) {

    suspend fun loadInstalledAppList(): List<AppInfo> = withContext(Dispatchers.IO) {
        val appContext = getApplication<Application>()
        val packageManager = appContext.packageManager
        packageManager.getInstalledPackages(0).map { packageInfo ->
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
        }
    }
}