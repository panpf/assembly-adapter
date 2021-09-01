package com.github.panpf.assemblyadapter.internal

import android.view.View
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.common.item.R

class OnClickListenerWrapper<DATA : Any>(
    val onClickListener: OnClickListener<DATA>
) : View.OnClickListener {

    override fun onClick(view: View) {
        @Suppress("UNCHECKED_CAST")
        val bindItem =
            view.getTag(R.id.aa_tag_clickBindItem) as Item<DATA>
        onClickListener.onClick(
            view.context,
            view,
            bindItem.bindingAdapterPosition,
            bindItem.absoluteAdapterPosition,
            bindItem.dataOrThrow
        )
    }
}