package me.panpf.adapter.sample.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.panpf.adapter.sample.ds.ListDataSource

class ListViewModel(application: Application) : MyAndroidViewModel(application) {
    val listStatus = MutableLiveData<ListStatus>()
    val list = makePagedList(ListDataSource.Factory(listStatus), listStatus)
}