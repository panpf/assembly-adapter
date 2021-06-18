package com.github.panpf.assemblyadapter.sample.ds

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OverviewInstallAppPinyinGroupPagerSource private constructor(
    private val context: Context, private val factory: Factory
) : PagingSource<Int, Any>() {

    class Factory(val context: Context) : Function0<OverviewInstallAppPinyinGroupPagerSource> {

        var appGroupList: List<AppGroup>? = null
        var lastPinyinFirstChar: Char? = null

        override fun invoke(): OverviewInstallAppPinyinGroupPagerSource {
            return OverviewInstallAppPinyinGroupPagerSource(context, this)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        factory.appGroupList = null
        factory.lastPinyinFirstChar = null
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        if (params !is LoadParams.Refresh) {
            withContext(Dispatchers.IO) {
                Thread.sleep(1500)
            }
        }
        preparationAppPackageList()
        val loadedAppGroupList = loadAppGroups(params.key!!, params.loadSize).let {
            if (params is LoadParams.Refresh<*>) {
                insertOverview(
                    AppsOverview.createAppsOverviewByAppGroup(factory.appGroupList!!),
                    it
                )
            } else {
                it
            }
        }
        return LoadResult.Page(
            loadedAppGroupList,
            null,
            if (loadedAppGroupList.isNotEmpty()) params.key!! + params.loadSize else null
        )
    }

    private suspend fun loadAppGroups(start: Int, size: Int): List<AppGroup> =
        withContext(Dispatchers.IO) {
            val appGroupList = factory.appGroupList!!
            val toIndex = (start + size - 1).coerceAtMost(appGroupList.size - 1)
            if (start < appGroupList.size && toIndex < appGroupList.size) {
                appGroupList.slice(start.rangeTo(toIndex))
            } else {
                emptyList()
            }
        }

    private suspend fun preparationAppPackageList() {
        if (factory.appGroupList?.isNotEmpty() != true) {
            withContext(Dispatchers.IO) {
                factory.appGroupList = groupByPinyin(loadInstalledAppList())
            }
        }
    }

    private suspend fun loadInstalledAppList(): List<AppInfo> = withContext(Dispatchers.IO) {
        context.packageManager.getInstalledPackages(0).mapNotNull { packageInfo ->
            AppInfo.fromPackageInfo(context, packageInfo)
        }
    }

    private suspend fun groupByPinyin(appList: List<AppInfo>): List<AppGroup> =
        withContext(Dispatchers.IO) {
            appList
                .groupBy { it.namePinyin.first().uppercase() }
                .map { it -> AppGroup(it.key, it.value.sortedBy { it.namePinyinLowerCase }) }
                .sortedBy { it.title }
        }

    private suspend fun insertOverview(
        appsOverview: AppsOverview,
        appGroupList: List<AppGroup>
    ): List<Any> = withContext(Dispatchers.IO) {
        ArrayList<Any>().apply {
            add(appsOverview)
            addAll(appGroupList)
        }
    }
}