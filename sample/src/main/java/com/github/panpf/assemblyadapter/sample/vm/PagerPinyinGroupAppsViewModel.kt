package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.utils.PinyinGroupAppsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PagerPinyinGroupAppsViewModel(application: Application) : AndroidViewModel(application) {

    val loadingData = MutableLiveData<Boolean>()
    val pinyinGroupAppListData = MutableLiveData<List<AppGroup>>()
    val appsOverviewData = MutableLiveData<AppsOverview>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadingData.postValue(true)

            val appsOverview = AppsOverview.build(getApplication())
            val appGroupList = PinyinGroupAppsHelper(getApplication()).getAll()
            pinyinGroupAppListData.postValue(appGroupList)
            /*
             * AppsOverview 数据延迟发送是为了测试主体数据 appGroupList 显示出来以后
             * 再用刷新 PagerAdapter 的方式在列表头部显示 AppsOverview 时 ViewPager 能否正确刷新
             */
            Thread.sleep(1500)
            appsOverviewData.postValue(appsOverview)

            loadingData.postValue(false)
        }
    }
}