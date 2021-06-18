package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.utils.PinyinFlatAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinyinFlatAppsViewModel(application: Application) : AndroidViewModel(application) {

    val pinyinFlatAppListData = MutableLiveData<List<Any>>()
    val loadingData = MutableLiveData<Boolean>()

    val appsOverviewData = MutableLiveData<AppsOverview>()

    init {
        refresh()
    }

    fun refresh() {
        refreshAppsOverview()
        refreshAppList()
    }

    private fun refreshAppList() {
        viewModelScope.launch {
            loadingData.postValue(true)
            val list = withContext(Dispatchers.IO) {
                PinyinFlatAppsHelper(getApplication()).getAll()
            }
            pinyinFlatAppListData.postValue(list)
            loadingData.postValue(false)
        }
    }

    private fun refreshAppsOverview() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appsOverviewData.postValue(AppsOverview.build(getApplication()))
            }
        }
    }
}