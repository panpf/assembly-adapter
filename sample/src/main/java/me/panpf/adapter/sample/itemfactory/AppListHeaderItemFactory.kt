package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView

import me.panpf.adapter.recycler.AssemblyRecyclerItem
import me.panpf.adapter.recycler.AssemblyRecyclerItemFactory
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bindView

class AppListHeaderItemFactory : AssemblyRecyclerItemFactory<AppListHeaderItemFactory.AppListHeaderItem>() {

    override fun isTarget(data: Any): Boolean {
        return data is String
    }

    override fun createAssemblyItem(viewGroup: ViewGroup): AppListHeaderItem {
        return AppListHeaderItem(R.layout.list_item_app_list_header, viewGroup)
    }

    inner class AppListHeaderItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyRecyclerItem<String>(itemLayoutId, parent) {
        val textView: TextView by bindView(R.id.text_appListHeaderItem)

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(i: Int, s: String) {
            textView.text = s
        }
    }
}
