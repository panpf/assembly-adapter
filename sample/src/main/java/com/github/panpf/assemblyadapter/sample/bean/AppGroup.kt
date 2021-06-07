package com.github.panpf.assemblyadapter.sample.bean

import com.github.panpf.assemblyadapter.list.AssemblyExpandableGroup

data class AppGroup(val title: String, val appList: List<AppInfo>) :
    AssemblyExpandableGroup {
    override fun getChildCount(): Int = appList.size

    override fun getChild(childPosition: Int): Any? = appList[childPosition]
}