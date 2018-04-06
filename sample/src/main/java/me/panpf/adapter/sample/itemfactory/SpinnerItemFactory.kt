package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView

import me.panpf.adapter.AssemblyListItem
import me.panpf.adapter.AssemblyListItemFactory
import me.panpf.adapter.sample.bindView

class SpinnerItemFactory : AssemblyListItemFactory<SpinnerItemFactory.SpinnerItem>() {

    override fun isTarget(data: Any): Boolean {
        return data is String
    }

    override fun createAssemblyItem(parent: ViewGroup): SpinnerItem {
        return SpinnerItem(android.R.layout.simple_list_item_1, parent)
    }

    class SpinnerItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyListItem<String>(itemLayoutId, parent) {
        val textView: TextView by bindView(android.R.id.text1)

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(position: Int, s: String) {
            textView.text = s
        }
    }
}
