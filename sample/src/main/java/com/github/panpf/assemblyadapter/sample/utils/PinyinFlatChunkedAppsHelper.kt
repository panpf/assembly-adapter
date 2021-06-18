package com.github.panpf.assemblyadapter.sample.utils

import android.content.Context
import android.content.pm.PackageManager
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.Apps
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import java.util.*

class PinyinFlatChunkedAppsHelper(private val context: Context) {

    // Contains the following class types: PinyinGroup、Apps
    private val pinyinFlatAppList = loadInstalledAppList()

    private fun loadInstalledAppList(): List<Any> {
        val packageInfoList =
            context.packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        val appPackageList = packageInfoList.mapNotNull { packageInfo ->
            context.packageManager.getLaunchIntentForPackage(packageInfo.packageName)
                ?: return@mapNotNull null
            AppInfo.fromPackageInfo(context, packageInfo)!!
        }.sortedBy { it.namePinyinLowerCase }

        val appGroupList = appPackageList
            .groupBy { it.namePinyinLowerCase.first().uppercase() }
            .map { it -> AppGroup(it.key, it.value.sortedBy { it.namePinyinLowerCase }) }
            .sortedBy { it.title }

        val resultList = ArrayList<Any>()
        appGroupList.forEach { appGroup ->
            resultList.add(PinyinGroup(appGroup.title.first().uppercase(), appGroup.appList.size))
            resultList.addAll(appGroup.appList.chunked(4).map { Apps(it) })
        }
        return resultList
    }

    val count = pinyinFlatAppList.size

    /**
     * Contains the following class types: PinyinGroup、Apps
     */
    fun getRange(fromIndex: Int, toIndexExclusive: Int): List<Any> {
        return pinyinFlatAppList.subList(fromIndex, toIndexExclusive)
    }

    /**
     * Contains the following class types: PinyinGroup、Apps
     */
    fun getAll() = pinyinFlatAppList
}