package com.github.panpf.assemblyadapter.sample.bean

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Parcelable
import com.github.panpf.assemblyadapter.recycler.paging.DiffKey
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class AppsOverview constructor(val count: Int, val userAppCount: Int, val groupCount: Int) :
    Parcelable,
    DiffKey {

    @IgnoredOnParcel
    override val diffKey: String = "AppsOverview"

    companion object {

        fun createAppsOverviewByAppGroup(appGroupList: List<AppGroup>): AppsOverview {
            val count = appGroupList.sumOf { it.appList.size }
            val userAppCount = appGroupList.sumOf { appGroup ->
                appGroup.appList.count { app -> !app.systemApp }
            }
            val groupCount = appGroupList.size
            return AppsOverview(count, userAppCount, groupCount)
        }

        fun build(context: Context): AppsOverview {
            var count = 0
            var userAppCount = 0
            val packageInfoList =
                context.packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
            val pinyinGroupCount = packageInfoList.mapNotNull { packageInfo ->
                context.packageManager.getLaunchIntentForPackage(packageInfo.packageName)
                    ?: return@mapNotNull null
                count++
                if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    userAppCount++
                }
                val name =
                    packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
                val namePinyinLowercase =
                    Pinyin.toPinyin(name, "").lowercase(Locale.getDefault())
                namePinyinLowercase.first()
            }.distinct().count()
            return AppsOverview(count, userAppCount, pinyinGroupCount)
        }
    }
}