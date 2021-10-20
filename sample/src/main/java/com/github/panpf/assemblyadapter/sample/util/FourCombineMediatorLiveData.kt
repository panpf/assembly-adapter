package com.github.panpf.assemblyadapter.sample.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class FourCombineMediatorLiveData<T1, T2, T3, T4>(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    initValue: Boolean = false
) : MediatorLiveData<FourCombine<T1, T2, T3, T4>>() {
    init {
        addSource(source1) {
            value = FourCombine(it, source2.value, source3.value, source4.value)
        }
        addSource(source2) {
            value = FourCombine(source1.value, it, source3.value, source4.value)
        }
        addSource(source3) {
            value = FourCombine(source1.value, source2.value, it, source4.value)
        }
        addSource(source4) {
            value = FourCombine(source1.value, source2.value, source3.value, it)
        }
        if (initValue) {
            value = FourCombine(source1.value, source2.value, source3.value, source4.value)
        }
    }
}

data class FourCombine<T1, T2, T3, T4>(val t1: T1?, val t2: T2?, val t3: T3?, val t4: T4?)