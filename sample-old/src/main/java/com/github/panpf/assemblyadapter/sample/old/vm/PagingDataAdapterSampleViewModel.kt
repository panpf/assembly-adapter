package com.github.panpf.assemblyadapter.sample.old.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.panpf.assemblyadapter.sample.old.ds.PagingDataAdapterSampleSource

class PagingDataAdapterSampleViewModel(application: Application) : AndroidViewModel(application) {

    val pagingDataFlow =
        Pager(PagingConfig(20, 2, false, 20), 0, { PagingDataAdapterSampleSource() }).flow

}