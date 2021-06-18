package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.utils.PinyinFlatAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinyinFlatAppsViewModel(application: Application) : AndroidViewModel(application) {

    val pinyinFlatAppListData = MutableLiveData<List<Any>>()
    val loadingData = MutableLiveData<Boolean>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val appsOverviewData = channelFlow {
        withContext(Dispatchers.IO) {
            send(AppsOverview.build(getApplication()))
        }
    }.asLiveData()

    init {
        viewModelScope.launch {
            loadingData.postValue(true)
            val list = withContext(Dispatchers.IO) {
                PinyinFlatAppsHelper(getApplication()).getAll()
            }
            pinyinFlatAppListData.postValue(list)
            loadingData.postValue(false)
        }
    }
}