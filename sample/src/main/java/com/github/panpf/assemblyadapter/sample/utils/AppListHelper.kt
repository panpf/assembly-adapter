package com.github.panpf.assemblyadapter.sample.utils

import android.content.Context
import android.content.pm.PackageManager
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import java.util.*

class AppListHelper(private val context: Context) {

    // Contains the following class types: PinyinGroup、AppInfo
    private val appList = loadInstalledAppList()

    private fun loadInstalledAppList(): List<AppInfo> {
        val packageInfoList =
            context.packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        return packageInfoList.mapNotNull { packageInfo ->
            context.packageManager.getLaunchIntentForPackage(packageInfo.packageName)
                ?: return@mapNotNull null
            AppInfo.fromPackageInfo(context, packageInfo)!!
        }.sortedBy { it.namePinyinLowerCase }
    }

    val count = appList.size

    fun getRange(fromIndex: Int, toIndexExclusive: Int): List<AppInfo> {
        return appList.subList(fromIndex, toIndexExclusive)
    }

    fun getAll(): List<AppInfo> = appList
}