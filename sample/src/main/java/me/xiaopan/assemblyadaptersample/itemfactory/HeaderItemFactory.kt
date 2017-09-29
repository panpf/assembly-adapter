package me.xiaopan.assemblyadaptersample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import me.xiaopan.assemblyadapter.AssemblyItem
import me.xiaopan.assemblyadapter.AssemblyItemFactory
import me.xiaopan.assemblyadaptersample.R

class HeaderItemFactory : AssemblyItemFactory<HeaderItemFactory.HeaderItem>() {
    override fun isTarget(data: Any): Boolean {
        return data is String
    }

    override fun createAssemblyItem(parent: ViewGroup): HeaderItem {
        return HeaderItem(R.layout.list_item_header, parent)
    }

    inner class HeaderItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<String>(itemLayoutId, parent) {

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(position: Int, s: String) {
            (itemView as TextView).text = s
        }
    }
}
