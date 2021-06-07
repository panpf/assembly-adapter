package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InstalledAppListPinyinFlatViewModel(application: Application) :
    BaseInstalledAppListViewModel(application) {

    val pinyinFlatAppListData = MutableLiveData<List<Any>>()
    val loadingData = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            loadingData.postValue(true)
            pinyinFlatAppListData.postValue(flatByPinyin(loadInstalledAppList()))
            loadingData.postValue(false)
        }
    }

    private suspend fun flatByPinyin(appList: List<AppInfo>): List<Any> =
        withContext(Dispatchers.IO) {
            val sortedAppList = appList.sortedBy { it.namePinyin }
            ArrayList<Any>().apply {
                var lastPinyinFirstChar: Char? = null
                sortedAppList.forEach { app ->
                    val namePinyinFirstChar = app.namePinyin.first()
                    if (lastPinyinFirstChar == null || namePinyinFirstChar != lastPinyinFirstChar) {
                        val namePinyinFirstCharUppercase = namePinyinFirstChar.uppercaseChar()
                        add(PinyinGroup(namePinyinFirstCharUppercase.toString()))
                        lastPinyinFirstChar = namePinyinFirstChar
                    }
                    add(app)
                }
            }
        }
}