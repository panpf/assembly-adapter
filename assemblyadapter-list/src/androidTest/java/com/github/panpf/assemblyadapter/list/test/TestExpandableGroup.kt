package com.github.panpf.assemblyadapter.list.test

import com.github.panpf.assemblyadapter.list.ExpandableGroup

class TestExpandableGroup(val name: String = "") : ExpandableGroup {
    override fun getChildCount(): Int = 0

    override fun getChild(childPosition: Int): Any {
        TODO("Not yet implemented")
    }
}