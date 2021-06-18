package com.github.panpf.assemblyadapter.sample.utils

import android.content.Context
import android.content.pm.PackageManager
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.Apps
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import java.util.*

class PinyinGroupAppsHelper(private val context: Context) {

    // Contains the following class types: AppGroup
    private val pinyinFlatAppList = loadInstalledAppList()

    private fun loadInstalledAppList(): List<AppGroup> {
        val packageInfoList =
            context.packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        val appPackageList = packageInfoList.mapNotNull { packageInfo ->
            context.packageManager.getLaunchIntentForPackage(packageInfo.packageName)
                ?: return@mapNotNull null
            AppInfo.fromPackageInfo(context, packageInfo)!!
        }.sortedBy { it.namePinyinLowerCase }

        return appPackageList
            .groupBy { it.namePinyinLowerCase.first().uppercase() }
            .map { it -> AppGroup(it.key, it.value.sortedBy { it.namePinyinLowerCase }) }
            .sortedBy { it.title }
    }

    val count = pinyinFlatAppList.size

    /**
     * Contains the following class types: AppGroup
     */
    fun getRange(fromIndex: Int, toIndexExclusive: Int): List<AppGroup> {
        return pinyinFlatAppList.subList(fromIndex, toIndexExclusive)
    }

    /**
     * Contains the following class types: AppGroup
     */
    fun getAll() = pinyinFlatAppList
}