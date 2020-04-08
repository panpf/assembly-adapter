package me.panpf.adapter.sample.item

import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.sample.R

class TextItem(parent: ViewGroup) : AssemblyItem<String>(R.layout.item_text, parent) {

    override fun onSetData(position: Int, s: String?) {
        (itemView as TextView).text = s
    }

    class Factory : AssemblyItemFactory<String>() {

        override fun match(data: Any?) = data is String

        override fun createAssemblyItem(parent: ViewGroup) = TextItem(parent)
    }
}
