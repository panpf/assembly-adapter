package me.panpf.adapter.sample.itemfactory

import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.sample.R

class HeaderGroupItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<String>(itemLayoutId, parent) {

    override fun onSetData(position: Int, s: String?) {
        (itemView as TextView).text = s
    }

    class Factory : AssemblyItemFactory<String>() {
        override fun match(data: Any?): Boolean {
            return data is String
        }

        override fun createAssemblyItem(parent: ViewGroup): HeaderGroupItem {
            return HeaderGroupItem(R.layout.list_item_header, parent)
        }
    }
}
