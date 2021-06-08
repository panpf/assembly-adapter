package com.github.panpf.assemblyadapter

import android.view.ViewGroup
import androidx.annotation.IdRes

interface ItemFactory<DATA> {
    fun match(data: Any?): Boolean

    fun dispatchCreateItem(parent: ViewGroup): Item<DATA>

    fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): ItemFactory<DATA>

    fun setOnViewLongClickListener(
        @IdRes viewId: Int, onLongClickListener: OnLongClickListener<DATA>
    ): ItemFactory<DATA>

    fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): ItemFactory<DATA>

    fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): ItemFactory<DATA>
}