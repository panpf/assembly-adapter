package com.github.panpf.assemblyadapter.sample.old.bean

import me.panpf.adapter.expandable.AssemblyGroup

class GameGroup : AssemblyGroup {
    var title: String? = null
    var gameList: ArrayList<Game>? = null

    override fun getChildCount(): Int {
        return if (gameList != null) gameList!!.size else 0
    }

    override fun getChild(childPosition: Int): Any? {
        return if (gameList != null && childPosition < gameList!!.size) gameList!![childPosition] else null
    }
}
