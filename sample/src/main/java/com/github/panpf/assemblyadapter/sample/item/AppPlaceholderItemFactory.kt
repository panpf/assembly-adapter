package com.github.panpf.assemblyadapter.sample.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.BindingPlaceholderItemFactory
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppPlaceholderBinding

class AppPlaceholderItemFactory :
    BindingPlaceholderItemFactory<ItemAppPlaceholderBinding>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemAppPlaceholderBinding {
        return ItemAppPlaceholderBinding.inflate(inflater, parent, false)
    }
}