package me.xiaopan.assemblyadaptersample.itemfactory

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView

import me.xiaopan.assemblyadapter.AssemblyItem
import me.xiaopan.assemblyadapter.AssemblyItemFactory
import me.xiaopan.assemblyadaptersample.bindView

class SpinnerItemFactory : AssemblyItemFactory<SpinnerItemFactory.SpinnerItem>() {

    override fun isTarget(data: Any): Boolean {
        return data is String
    }

    override fun createAssemblyItem(parent: ViewGroup): SpinnerItem {
        return SpinnerItem(android.R.layout.simple_list_item_1, parent)
    }

    class SpinnerItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<String>(itemLayoutId, parent) {
        val textView: TextView by bindView(android.R.id.text1)

        override fun onConfigViews(context: Context) {

        }

        override fun onSetData(position: Int, s: String) {
            textView.text = s
        }
    }
}
