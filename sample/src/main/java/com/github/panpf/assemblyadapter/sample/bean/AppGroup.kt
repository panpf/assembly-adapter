package com.github.panpf.assemblyadapter.sample.bean

import android.os.Parcelable
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.recycler.paging.DiffKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppGroup(
    val title: String, val appList: List<AppInfo>
) : ExpandableGroup, DiffKey, Parcelable {

    @IgnoredOnParcel
    override val diffKey: String = title

    override fun getChildCount(): Int = appList.size

    override fun getChild(childPosition: Int): Any? = appList[childPosition]
}