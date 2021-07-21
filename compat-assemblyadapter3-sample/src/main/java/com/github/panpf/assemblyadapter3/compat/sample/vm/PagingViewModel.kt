package com.github.panpf.assemblyadapter3.compat.sample.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.panpf.assemblyadapter3.compat.sample.ds.PagingSamplePagingSource

class PagingViewModel(application: Application) : AndroidViewModel(application) {
    val pagingDataFlow = Pager(
        PagingConfig(20, 1, false, 20),
        0,
        { PagingSamplePagingSource(application) }
    ).flow
}