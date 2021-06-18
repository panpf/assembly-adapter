package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.ds.InstallAppPinyinFlatPagerSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

class InstalledAppPinyinFlatPagingViewModel(application: Application) :
    AndroidViewModel(application) {

    val pinyinFlatAppListDataFlow =
        Pager(
            PagingConfig(20, 5, false, 20),
            0,
            InstallAppPinyinFlatPagerSource.Factory(getApplication())
        ).flow

    @OptIn(ExperimentalCoroutinesApi::class)
    val appsOverviewData = channelFlow {
        withContext(Dispatchers.IO) {
            send(AppsOverview.build(getApplication()))
        }
    }.asLiveData()
}