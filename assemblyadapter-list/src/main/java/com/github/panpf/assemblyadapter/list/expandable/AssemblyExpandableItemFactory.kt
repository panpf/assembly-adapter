package com.github.panpf.assemblyadapter.list.expandable

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.AssemblyItemFactory

abstract class AssemblyExpandableItemFactory<DATA> : AssemblyItemFactory<DATA>() {

    abstract override fun createItem(parent: ViewGroup): AssemblyExpandableItem<DATA>
}