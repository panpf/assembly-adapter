package com.github.panpf.assemblyadapter

import android.view.ViewGroup

abstract class PlaceholderItemFactory : ItemFactory<Placeholder>() {

    final override fun match(data: Any): Boolean = data is Placeholder

    abstract override fun createItem(parent: ViewGroup): PlaceholderItem
}