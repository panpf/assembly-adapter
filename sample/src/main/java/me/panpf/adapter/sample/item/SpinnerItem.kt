package me.panpf.adapter.sample.item

import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView

class SpinnerItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<String>(itemLayoutId, parent) {
    private val textView: TextView by bindView(android.R.id.text1)

    override fun onSetData(position: Int, s: String?) {
        textView.text = s
    }

    class Factory : AssemblyItemFactory<String>() {

        override fun match(data: Any?): Boolean {
            return data is String
        }

        override fun createAssemblyItem(parent: ViewGroup): SpinnerItem {
            return SpinnerItem(android.R.layout.simple_list_item_1, parent)
        }
    }
}
