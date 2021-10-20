package com.github.panpf.assemblyadapter.sample.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class ThreeCombineMediatorLiveData<T1, T2, T3>(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    initValue: Boolean = false
) : MediatorLiveData<ThreeCombine<T1, T2, T3>>() {
    init {
        addSource(source1) {
            value = ThreeCombine(it, source2.value, source3.value)
        }
        addSource(source2) {
            value = ThreeCombine(source1.value, it, source3.value)
        }
        addSource(source3) {
            value = ThreeCombine(source1.value, source2.value, it)
        }
        if (initValue) {
            value = ThreeCombine(source1.value, source2.value, source3.value)
        }
    }
}

data class ThreeCombine<T1, T2, T3>(val t1: T1?, val t2: T2?, val t3: T3?)