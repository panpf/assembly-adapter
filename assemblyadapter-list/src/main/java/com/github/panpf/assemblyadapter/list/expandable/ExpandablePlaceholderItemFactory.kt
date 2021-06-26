package com.github.panpf.assemblyadapter.list.expandable

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.PlaceholderItemFactory

abstract class ExpandablePlaceholderItemFactory : PlaceholderItemFactory() {

    abstract override fun createItem(parent: ViewGroup): ExpandablePlaceholderItem
}