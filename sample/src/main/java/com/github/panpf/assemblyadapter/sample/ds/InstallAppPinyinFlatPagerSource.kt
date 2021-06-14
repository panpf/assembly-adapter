package com.github.panpf.assemblyadapter.sample.ds

import android.content.Context
import android.content.pm.PackageManager
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class InstallAppPinyinFlatPagerSource private constructor(
    private val context: Context, private val factory: Factory
) : PagingSource<Int, Any>() {

    class Factory(val context: Context) : Function0<InstallAppPinyinFlatPagerSource> {

        var appPackageList: List<Pair<String, String>>? = null
        var lastPinyinFirstChar: Char? = null

        override fun invoke(): InstallAppPinyinFlatPagerSource {
            return InstallAppPinyinFlatPagerSource(context, this)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        factory.appPackageList = null
        factory.lastPinyinFirstChar = null
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        preparationAppPackageList()
        return LoadResult.Page(
            flatByPinyin(loadApps(params.key!!, params.loadSize)),
            null,
            params.key!! + params.loadSize
        )
    }

    private suspend fun preparationAppPackageList() {
        if (factory.appPackageList?.isNotEmpty() != true) {
            withContext(Dispatchers.IO) {
                val packageInfoList =
                    context.packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
                factory.appPackageList = packageInfoList.mapNotNull { packageInfo ->
                    context.packageManager.getLaunchIntentForPackage(packageInfo.packageName)
                        ?: return@mapNotNull null
                    val name =
                        packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
                    val namePinyinLowercase =
                        Pinyin.toPinyin(name, "").lowercase(Locale.getDefault())
                    packageInfo.packageName to namePinyinLowercase
                }.sortedBy { it.second }
            }
        }
    }

    private suspend fun loadApps(start: Int, size: Int): List<AppInfo> =
        withContext(Dispatchers.IO) {
            val appPackageList = factory.appPackageList!!
            val toIndex = (start + size - 1).coerceAtMost(appPackageList.size - 1)
            if (start < appPackageList.size && toIndex < appPackageList.size) {
                appPackageList.slice(start.rangeTo(toIndex)).map { packageName ->
                    val packageInfo = context.packageManager.getPackageInfo(packageName.first, 0)
                    AppInfo.fromPackageInfo(context, packageInfo)!!
                }
            } else {
                emptyList()
            }
        }

    private suspend fun flatByPinyin(appList: List<AppInfo>): List<Any> =
        withContext(Dispatchers.IO) {
            ArrayList<Any>().apply {
                appList.forEach { app ->
                    val namePinyinFirstChar = app.namePinyin.first().uppercaseChar()
                    if (factory.lastPinyinFirstChar == null || namePinyinFirstChar != factory.lastPinyinFirstChar) {
                        add(PinyinGroup(namePinyinFirstChar.toString()))
                        factory.lastPinyinFirstChar = namePinyinFirstChar
                    }
                    add(app)
                }
            }
        }
}