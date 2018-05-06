package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory

import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bindView

class AppListHeaderItemFactory : AssemblyItemFactory<String>() {

    override fun match(data: Any?): Boolean {
        return data is String
    }

    override fun createAssemblyItem(viewGroup: ViewGroup): AppListHeaderItem {
        return AppListHeaderItem(R.layout.list_item_app_list_header, viewGroup)
    }

    inner class AppListHeaderItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<String>(itemLayoutId, parent) {
        private val textView: TextView by bindView(R.id.text_appListHeaderItem)

        override fun onConfigViews(context: Context) {
        }

        override fun onSetData(i: Int, s: String?) {
            textView.text = s
        }
    }
}
