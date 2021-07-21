package com.github.panpf.assemblyadapter3.compat.sample.item

import android.view.ViewGroup
import android.widget.TextView
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItem
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItemFactory
import com.github.panpf.assemblyadapter3.compat.sample.R

class TextItem(parent: ViewGroup) : CompatAssemblyItem<String>(R.layout.item_text, parent) {

    override fun onSetData(position: Int, data: String?) {
        (itemView as TextView).text = data
    }

    class Factory : CompatAssemblyItemFactory<String>() {

        override fun match(data: Any?) = data is String

        override fun createAssemblyItem(parent: ViewGroup) = TextItem(parent)
    }
}
