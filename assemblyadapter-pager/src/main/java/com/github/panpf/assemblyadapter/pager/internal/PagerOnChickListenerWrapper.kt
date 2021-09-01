package com.github.panpf.assemblyadapter.pager.internal

import android.view.View
import com.github.panpf.assemblyadapter.OnClickListener

class PagerOnChickListenerWrapper<DATA : Any>(
    val onClickListener: OnClickListener<DATA>,
    val bindingAdapterPosition: Int,
    val absoluteAdapterPosition: Int,
    val data: DATA
) : View.OnClickListener {

    override fun onClick(view: View) {
        @Suppress("UNCHECKED_CAST")
        onClickListener.onClick(
            view.context,
            view,
            bindingAdapterPosition,
            absoluteAdapterPosition,
            data
        )
    }
}