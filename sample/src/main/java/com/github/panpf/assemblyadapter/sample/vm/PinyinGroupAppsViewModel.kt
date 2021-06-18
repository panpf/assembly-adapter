package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.utils.PinyinGroupAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinyinGroupAppsViewModel(application: Application) : AndroidViewModel(application) {

    val pinyinGroupAppListData = MutableLiveData<List<AppGroup>>()
    val loadingData = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            loadingData.postValue(true)
            val list = withContext(Dispatchers.IO) {
                PinyinGroupAppsHelper(getApplication()).getAll()
            }
            pinyinGroupAppListData.postValue(list)
            loadingData.postValue(false)
        }
    }
}