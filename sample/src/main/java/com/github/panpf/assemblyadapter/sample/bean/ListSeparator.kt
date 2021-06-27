package com.github.panpf.assemblyadapter.sample.bean

import android.os.Parcelable
import com.github.panpf.assemblyadapter.recycler.paging.DiffKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListSeparator(val title: String) : DiffKey, Parcelable {

    @IgnoredOnParcel
    override val diffKey: String = title
}