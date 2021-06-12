package com.github.panpf.assemblyadapter.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.panpf.assemblyadapter.sample.ds.InstallAppPagerSource

class InstalledAppPinyinFlatPagingViewModel(application: Application) :
    AndroidViewModel(application) {

    val pinyinFlatAppListDataFlow =
        Pager(
            PagingConfig(20, 5, false, 40),
            0,
            InstallAppPagerSource.Factory(getApplication())
        ).flow
}