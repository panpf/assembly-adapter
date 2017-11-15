package me.panpf.assemblyadapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView

import me.panpf.assemblyadapter.AssemblyRecyclerItem
import me.panpf.assemblyadapter.AssemblyRecyclerItemFactory
import me.panpf.assemblyadapter.sample.R

class HeaderRecyclerItemFactory : AssemblyRecyclerItemFactory<HeaderRecyclerItemFactory.HeaderRecyclerItem>() {
    override fun isTarget(data: Any): Boolean {
        return data is String
    }

    override fun createAssemblyItem(parent: ViewGroup): HeaderRecyclerItem {
        return HeaderRecyclerItem(R.layout.list_item_header, parent)
    }

    inner class HeaderRecyclerItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyRecyclerItem<String>(itemLayoutId, parent) {

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(position: Int, s: String) {
            (itemView as TextView).text = s
        }
    }
}
