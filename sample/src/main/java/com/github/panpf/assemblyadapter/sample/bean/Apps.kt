package com.github.panpf.assemblyadapter.sample.bean

import android.os.Parcelable
import com.github.panpf.assemblyadapter.list.AssemblyExpandableGroup
import com.github.panpf.assemblyadapter.recycler.paging.DiffKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Apps(
    val appList: List<AppInfo>
) : DiffKey, Parcelable {

    @IgnoredOnParcel
    override val diffKey: String = appList.joinToString { it.packageName }
}