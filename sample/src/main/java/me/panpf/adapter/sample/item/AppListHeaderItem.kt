package me.panpf.adapter.sample.item

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.adapter.AssemblyStickyRecyclerAdapter
import me.panpf.adapter.sample.bean.AppsTitle

class AppListHeaderItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<AppsTitle>(itemLayoutId, parent) {
    private val textView: TextView by bindView(R.id.text_appListHeaderItem)
    private val more: TextView by bindView(R.id.text_appListHeaderItem_more)
    private val expand: TextView by bindView(R.id.text_appListHeaderItem_expand)

    override fun onConfigViews(context: Context) {
        super.onConfigViews(context)

        expand.setOnClickListener {
            data?.run {
                expand = !expand
                more.visibility = if (expand) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onSetData(i: Int, data: AppsTitle?) {
        textView.text = data?.title
        more.visibility = if (data?.expand == true) View.VISIBLE else View.GONE
    }

    class Factory : AssemblyItemFactory<AppsTitle>(), AssemblyStickyRecyclerAdapter.StickyItemFactory {

        override fun match(data: Any?): Boolean {
            return data is AppsTitle
        }

        override fun createAssemblyItem(viewGroup: ViewGroup): AppListHeaderItem {
            return AppListHeaderItem(R.layout.list_item_app_list_header, viewGroup)
        }
    }
}
