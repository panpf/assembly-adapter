package me.panpf.adapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import android.content.pm.ApplicationInfo
import android.text.format.Formatter
import kotlinx.coroutines.experimental.launch
import me.panpf.adapter.sample.bean.AppInfo
import java.io.File

class AppsViewModel(application: Application) : AndroidViewModel(application) {
    val apps = MutableLiveData<Array<List<AppInfo>>>()

    fun load() {
        launch {
            val appContext = getApplication<Application>()
            val packageManager = appContext.packageManager
            val packageInfoList = packageManager.getInstalledPackages(0)
            val systemAppList = ArrayList<AppInfo>()
            val userAppList = ArrayList<AppInfo>()
            for (packageInfo in packageInfoList) {
                val appInfo = AppInfo(true)
                appInfo.packageName = packageInfo.packageName
                appInfo.name = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                appInfo.sortName = appInfo.name
                appInfo.id = packageInfo.packageName
                appInfo.versionName = packageInfo.versionName
                appInfo.apkFilePath = packageInfo.applicationInfo.publicSourceDir
                appInfo.appSize = Formatter.formatFileSize(appContext, File(appInfo.apkFilePath).length())
                appInfo.versionCode = packageInfo.versionCode
                if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                    systemAppList.add(appInfo)
                } else {
                    userAppList.add(appInfo)
                }
            }

            systemAppList.sortWith(Comparator { lhs, rhs ->
                (lhs.sortName ?: "").compareTo(rhs.sortName ?: "")
            })

            apps.postValue(arrayOf(systemAppList, userAppList))
        }
    }
}