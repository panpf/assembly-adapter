package com.github.panpf.assemblyadapter3.compat.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.github.panpf.assemblyadapter3.compat.sample.util.PinyinFlatAppsHelper
import com.github.panpf.assemblyadapter3.compat.sample.util.minExecuteTime

class RecyclerLinearViewModel(application: Application) : AndroidViewModel(application) {

    private val appHelper by lazy { PinyinFlatAppsHelper(application) }
    private var index = 0
    val size = 20
    val refreshAppListData = MutableLiveData<List<Any>>()
    val appendAppListData = MutableLiveData<List<Any>>()
    val refreshingData = MutableLiveData<Boolean>()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            refreshingData.postValue(true)
            index = 0
            val result = load(index, size)
            refreshAppListData.postValue(result!!)
            index += result.size
            refreshingData.postValue(false)
        }
    }

    fun apppend() {
        viewModelScope.launch {
            val result = load(index, size)
            appendAppListData.postValue(result!!)
            index += result.size
        }
    }

    /**
     * Contains the following class types: ListSeparator、AppInfo
     */
    private suspend fun load(startIndex: Int, size: Int): List<Any> {
        return withContext(Dispatchers.IO) {
            minExecuteTime(1500) {
                appHelper.getRange(startIndex, startIndex + size)
            }
        }
    }
}