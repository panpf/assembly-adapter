package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyListItem
import me.panpf.adapter.AssemblyListItemFactory
import me.panpf.adapter.sample.R

class HeaderItemFactory : AssemblyListItemFactory<HeaderItemFactory.HeaderItem>() {
    override fun isTarget(data: Any): Boolean {
        return data is String
    }

    override fun createAssemblyItem(parent: ViewGroup): HeaderItem {
        return HeaderItem(R.layout.list_item_header, parent)
    }

    inner class HeaderItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyListItem<String>(itemLayoutId, parent) {

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(position: Int, s: String) {
            (itemView as TextView).text = s
        }
    }
}
