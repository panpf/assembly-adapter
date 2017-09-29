package me.xiaopan.assemblyadaptersample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView

import me.xiaopan.assemblyadapter.AssemblyGroupItem
import me.xiaopan.assemblyadapter.AssemblyGroupItemFactory
import me.xiaopan.assemblyadaptersample.R

class HeaderGroupItemFactory : AssemblyGroupItemFactory<HeaderGroupItemFactory.HeaderGroupItem>() {
    override fun isTarget(data: Any): Boolean {
        return data is String
    }

    override fun createAssemblyItem(parent: ViewGroup): HeaderGroupItem {
        return HeaderGroupItem(R.layout.list_item_header, parent)
    }

    inner class HeaderGroupItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyGroupItem<String>(itemLayoutId, parent) {

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(position: Int, s: String) {
            (itemView as TextView).text = s
        }
    }
}
