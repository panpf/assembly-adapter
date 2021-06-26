package com.github.panpf.assemblyadapter

import android.view.ViewGroup

abstract class AssemblyPlaceholderItemFactory : AssemblyItemFactory<Placeholder>() {

    final override fun match(data: Any): Boolean = data is Placeholder

    abstract override fun createItem(parent: ViewGroup): AssemblyPlaceholderItem
}