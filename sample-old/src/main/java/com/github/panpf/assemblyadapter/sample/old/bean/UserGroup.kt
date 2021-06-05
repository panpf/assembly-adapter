package com.github.panpf.assemblyadapter.sample.old.bean

import me.panpf.adapter.expandable.AssemblyGroup

class UserGroup : AssemblyGroup {
    var title: String? = null
    var userList: ArrayList<User>? = null

    override fun getChildCount(): Int {
        return if (userList != null) userList!!.size else 0
    }

    override fun getChild(childPosition: Int): Any? {
        return if (userList != null && childPosition < userList!!.size) userList!![childPosition] else null
    }
}