package com.github.panpf.assemblyadapter.list.expandable

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.ItemFactory

abstract class ExpandableItemFactory<DATA> : ItemFactory<DATA>() {

    abstract override fun createItem(parent: ViewGroup): ExpandableItem<DATA>
}