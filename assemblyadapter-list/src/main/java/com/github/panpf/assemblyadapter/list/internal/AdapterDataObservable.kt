package com.github.panpf.assemblyadapter.list.internal

import android.database.DataSetObserver
import android.database.Observable

class AdapterDataObservable : Observable<DataSetObserver>() {
    fun hasObservers(): Boolean {
        return mObservers.isNotEmpty()
    }
}