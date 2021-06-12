package com.github.panpf.assemblyadapter.sample.bean

import com.github.panpf.assemblyadapter.list.AssemblyExpandableGroup
import com.github.panpf.assemblyadapter.paging.DiffKey

data class AppGroup(
    val title: String, val appList: List<AppInfo>
) : AssemblyExpandableGroup, DiffKey {

    override val diffKey: String = title

    override fun getChildCount(): Int = appList.size

    override fun getChild(childPosition: Int): Any? = appList[childPosition]
}