package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.ds.PinyinFlatAppsPagerSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

class PinyinFlatPagingAppsViewModel(application: Application) :
    AndroidViewModel(application) {

    /**
     * Contains the following class types: PinyinGroup、AppInfo
     */
    val pinyinFlatAppListDataFlow: Flow<PagingData<Any>> =
        Pager(
            PagingConfig(20, 5, false, 20),
            0,
            PinyinFlatAppsPagerSource.Factory(getApplication())
        ).flow

    @OptIn(ExperimentalCoroutinesApi::class)
    val appsOverviewData: LiveData<AppsOverview> = channelFlow {
        withContext(Dispatchers.IO) {
            send(AppsOverview.build(getApplication()))
        }
    }.asLiveData()
}