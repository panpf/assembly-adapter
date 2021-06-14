package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.panpf.assemblyadapter.sample.ds.OverviewInstallAppPinyinGroupPagerSource

class OverviewInstalledAppPinyinGroupPagingViewModel(application: Application) :
    AndroidViewModel(application) {

    val pinyinGroupAppListDataFlow =
        Pager(
            PagingConfig(5, 1, false, 10),
            0,
            OverviewInstallAppPinyinGroupPagerSource.Factory(getApplication())
        ).flow
}