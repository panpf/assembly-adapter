package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.ds.PinyinFlatAppsPagerSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
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

    val appsOverviewData = MutableLiveData<AppsOverview>()

    init {
        refresh()
    }

    fun refresh() {
        refreshAppsOverview()
    }

    private fun refreshAppsOverview() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appsOverviewData.postValue(AppsOverview.build(getApplication()))
            }
        }
    }
}