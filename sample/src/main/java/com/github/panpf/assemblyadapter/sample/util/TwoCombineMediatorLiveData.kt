package com.github.panpf.assemblyadapter.sample.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class TwoCombineMediatorLiveData<T1, T2>(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    initValue: Boolean = false
) : MediatorLiveData<TwoCombine<T1, T2>>() {
    init {
        addSource(source1) {
            value = TwoCombine(it, source2.value)
        }
        addSource(source2) {
            value = TwoCombine(source1.value, it)
        }
        if (initValue) {
            value = TwoCombine(source1.value, source2.value)
        }
    }
}

data class TwoCombine<T1, T2>(val t1: T1?, val t2: T2?)