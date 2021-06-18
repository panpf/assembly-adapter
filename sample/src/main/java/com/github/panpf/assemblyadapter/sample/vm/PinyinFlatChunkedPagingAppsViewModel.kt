package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.ds.PinyinFlatChunkedAppsPagerSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

class PinyinFlatChunkedPagingAppsViewModel(application: Application) :
    AndroidViewModel(application) {

    /**
     * Contains the following class types: PinyinGroup、Apps
     */
    val pinyinFlatChunkedAppListDataFlow: Flow<PagingData<Any>> =
        Pager(
            PagingConfig(5, 1, false, 10),
            0,
            PinyinFlatChunkedAppsPagerSource.Factory(getApplication())
        ).flow

    @OptIn(ExperimentalCoroutinesApi::class)
    val appsOverviewData: LiveData<AppsOverview> = channelFlow {
        withContext(Dispatchers.IO) {
            send(AppsOverview.build(getApplication()))
        }
    }.asLiveData()
}