package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.utils.PinyinFlatChunkedAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinyinFlatChunkedAppsViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Contains the following class types: PinyinGroup、Apps
     */
    val pinyinFlatChunkedAppListData = MutableLiveData<List<Any>>()
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
                PinyinFlatChunkedAppsHelper(getApplication()).getAll()
            }
            pinyinFlatChunkedAppListData.postValue(list)
            loadingData.postValue(false)
        }
    }
}