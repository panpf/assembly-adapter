package me.panpf.adapter.sample.itemfactory

import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import me.panpf.adapter.sample.R

class AppListHeaderItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<String>(itemLayoutId, parent) {
    private val textView: TextView by bindView(R.id.text_appListHeaderItem)

    override fun onSetData(i: Int, s: String?) {
        textView.text = s
    }

    class Factory : AssemblyItemFactory<String>() {

        override fun match(data: Any?): Boolean {
            return data is String
        }

        override fun createAssemblyItem(viewGroup: ViewGroup): AppListHeaderItem {
            return AppListHeaderItem(R.layout.list_item_app_list_header, viewGroup)
        }
    }
}
