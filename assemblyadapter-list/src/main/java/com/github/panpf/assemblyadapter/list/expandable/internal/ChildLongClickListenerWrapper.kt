package com.github.panpf.assemblyadapter.list.expandable.internal

import android.view.View
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.list.expandable.ExpandableChildItem
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.OnChildLongClickListener

class ChildLongClickListenerWrapper<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any>(
    private val onChildLongClickListener: OnChildLongClickListener<GROUP_DATA, CHILD_DATA>
) : View.OnLongClickListener {

    override fun onLongClick(view: View): Boolean {
        @Suppress("UNCHECKED_CAST")
        val bindItem =
            view.getTag(R.id.aa_tag_clickBindItem) as ExpandableChildItem<GROUP_DATA, CHILD_DATA>
        return onChildLongClickListener.onLongClick(
            view.context,
            view,
            bindItem.groupBindingAdapterPosition,
            bindItem.groupAbsoluteAdapterPosition,
            bindItem.groupDataOrThrow,
            bindItem.isLastChild,
            bindItem.bindingAdapterPosition,
            bindItem.absoluteAdapterPosition,
            bindItem.dataOrThrow
        )
    }
}