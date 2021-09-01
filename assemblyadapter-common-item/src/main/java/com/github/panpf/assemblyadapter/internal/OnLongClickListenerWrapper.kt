package com.github.panpf.assemblyadapter.internal

import android.view.View
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.common.item.R

class OnLongClickListenerWrapper<DATA : Any>(
    val onLongClickListener: OnLongClickListener<DATA>
) : View.OnLongClickListener {

    override fun onLongClick(view: View): Boolean {
        @Suppress("UNCHECKED_CAST")
        val bindItem =
            view.getTag(R.id.aa_tag_clickBindItem) as Item<DATA>
        return onLongClickListener.onLongClick(
            view.context,
            view,
            bindItem.bindingAdapterPosition,
            bindItem.absoluteAdapterPosition,
            bindItem.dataOrThrow
        )
    }
}