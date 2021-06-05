package com.github.panpf.assemblyadapter.sample.old.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

open class MyAndroidViewModel(application: Application) : AndroidViewModel(application) {
    private fun makePagedListConfig(pageSize: Int = 20): PagedList.Config =
        PagedList.Config.Builder().apply {
            setPageSize(pageSize)
            setInitialLoadSizeHint(pageSize)
            setPrefetchDistance(2)
            setEnablePlaceholders(false)
        }.build()

    fun <Key : Any, Value : Any> makePagedList(
        dataSourceFactory: DataSource.Factory<Key, Value>,
        listStatus: MutableLiveData<ListStatus>, pageSize: Int = 20
    ): LiveData<PagedList<Value>> =
        LivePagedListBuilder<Key, Value>(dataSourceFactory, makePagedListConfig(pageSize))
            .setBoundaryCallback(ListBoundaryCallback(listStatus))
            .build()
}