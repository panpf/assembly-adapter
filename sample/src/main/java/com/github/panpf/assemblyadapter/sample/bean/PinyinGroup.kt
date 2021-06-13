package com.github.panpf.assemblyadapter.sample.bean

import com.github.panpf.assemblyadapter.recycler.paging.DiffKey

data class PinyinGroup(val title: String) : DiffKey {

    override val diffKey: String = title
}