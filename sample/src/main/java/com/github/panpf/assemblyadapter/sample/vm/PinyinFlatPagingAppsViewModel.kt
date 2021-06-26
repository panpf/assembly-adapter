package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.panpf.assemblyadapter.sample.ds.AppListPagerSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            AppListPagerSource.Factory(getApplication())
        ).flow.map {
            it.insertSeparators { before: AppInfo?, after: AppInfo? ->
                withContext(Dispatchers.IO) {
                    when {
                        before != null && after != null -> {
                            val beforeFirChar = before.namePinyinLowerCase.first().uppercaseChar()
                            val afterFirChar = after.namePinyinLowerCase.first().uppercaseChar()
                            if (beforeFirChar != afterFirChar) PinyinGroup(afterFirChar.toString()) else null
                        }
                        before == null && after != null -> {
                            PinyinGroup(after.namePinyinLowerCase.first().uppercase())
                        }
                        else -> null
                    }
                }
            }
        }

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