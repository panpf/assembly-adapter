package com.github.panpf.assemblyadapter.sample.bean

import android.os.Parcelable
import com.github.panpf.assemblyadapter.recycler.paging.DiffKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppsOverview(val count: Int, val userAppCount: Int, val groupCount: Int) : Parcelable, DiffKey {

    @IgnoredOnParcel
    override val diffKey: String = "AppsOverview"
}