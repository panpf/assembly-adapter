package com.github.panpf.assemblyadapter.list.expandable

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.AssemblyPlaceholderItemFactory

abstract class AssemblyExpandablePlaceholderItemFactory : AssemblyPlaceholderItemFactory() {

    abstract override fun createItem(parent: ViewGroup): AssemblyExpandablePlaceholderItem
}