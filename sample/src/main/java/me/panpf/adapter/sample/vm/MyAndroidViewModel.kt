package me.panpf.adapter.sample.vm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList

open class MyAndroidViewModel(application: Application) : AndroidViewModel(application) {
    private fun makePagedListConfig(pageSize: Int = 20): PagedList.Config =
            PagedList.Config.Builder().apply {
                setPageSize(pageSize)
                setInitialLoadSizeHint(pageSize)
                setPrefetchDistance(2)
                setEnablePlaceholders(false)
            }.build()

    fun <Key, Value> makePagedList(dataSourceFactory: DataSource.Factory<Key, Value>,
                                   listStatus: MutableLiveData<ListStatus>, pageSize: Int = 20): LiveData<PagedList<Value>> =
            LivePagedListBuilder<Key, Value>(dataSourceFactory, makePagedListConfig(pageSize))
                    .setBoundaryCallback(ListBoundaryCallback(listStatus))
                    .build()
}