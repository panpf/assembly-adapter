package com.github.panpf.assemblyadapter.list.expandable.test

import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup

class TestExpandableGroup(val name: String = "") : ExpandableGroup {
    override fun getChildCount(): Int = 0

    override fun getChild(childPosition: Int): Any {
        TODO("Not yet implemented")
    }
}