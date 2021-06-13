package com.github.panpf.assemblyadapter.test

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener

class TestItemFactory<DATA> : ItemFactory<DATA> {

    override fun match(data: Any?): Boolean {
        return false
    }

    override fun dispatchCreateItem(parent: ViewGroup): Item<DATA> {
        return TestItem(parent.context)
    }

    override fun setOnViewClickListener(
        viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): ItemFactory<DATA> {
        return this
    }

    override fun setOnViewLongClickListener(
        viewId: Int,
        onLongClickListener: OnLongClickListener<DATA>
    ): ItemFactory<DATA> {
        return this
    }

    override fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): ItemFactory<DATA> {
        return this
    }

    override fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): ItemFactory<DATA> {
        return this
    }
}