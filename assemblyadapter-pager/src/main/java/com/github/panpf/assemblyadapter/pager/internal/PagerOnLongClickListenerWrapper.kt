package com.github.panpf.assemblyadapter.pager.internal

import android.view.View
import com.github.panpf.assemblyadapter.OnLongClickListener

class PagerOnLongClickListenerWrapper<DATA : Any>(
    val onLongClickListener: OnLongClickListener<DATA>,
    val bindingAdapterPosition: Int,
    val absoluteAdapterPosition: Int,
    val data: DATA
) : View.OnLongClickListener {

    override fun onLongClick(view: View): Boolean {
        @Suppress("UNCHECKED_CAST")
        return onLongClickListener.onLongClick(
            view.context,
            view,
            bindingAdapterPosition,
            absoluteAdapterPosition,
            data
        )
    }
}