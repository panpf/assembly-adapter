package com.github.panpf.assemblyadapter.sample.old.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.github.panpf.assemblyadapter.sample.old.base.ListStatus
import com.github.panpf.assemblyadapter.sample.old.base.MyAndroidViewModel
import com.github.panpf.assemblyadapter.sample.old.ds.ListDataSource

class ListViewModel(application: Application) : MyAndroidViewModel(application) {
    val listStatus = MutableLiveData<ListStatus>()
    val list = makePagedList(ListDataSource.Factory(listStatus), listStatus)
}