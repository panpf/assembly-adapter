package me.panpf.adapter.sample.item

import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.adapter.AssemblyStickyRecyclerAdapter
import me.panpf.adapter.sample.bean.AppsTitle

class AppListHeaderItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<AppsTitle>(itemLayoutId, parent) {
    private val textView: TextView by bindView(R.id.appHeaderItem_titleText)

    override fun onSetData(i: Int, data: AppsTitle?) {
        textView.text = data?.title
    }

    class Factory : AssemblyItemFactory<AppsTitle>(), AssemblyStickyRecyclerAdapter.StickyItemFactory {

        override fun match(data: Any?): Boolean {
            return data is AppsTitle
        }

        override fun createAssemblyItem(viewGroup: ViewGroup): AppListHeaderItem {
            return AppListHeaderItem(R.layout.item_app_header, viewGroup)
        }
    }
}
