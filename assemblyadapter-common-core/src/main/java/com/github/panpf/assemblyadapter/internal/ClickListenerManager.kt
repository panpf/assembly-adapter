package com.github.panpf.assemblyadapter.internal

import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import java.util.*

class ClickListenerManager<DATA> {

    val holders: MutableList<Any> = LinkedList()

    fun add(@IdRes viewId: Int, onClickListener: OnClickListener<DATA>) {
        holders.add(ClickListenerHolder(viewId, onClickListener))
    }

    fun add(onClickListener: OnClickListener<DATA>) {
        holders.add(ClickListenerHolder(onClickListener))
    }

    fun add(@IdRes viewId: Int, onClickListener: OnLongClickListener<DATA>) {
        holders.add(LongClickListenerHolder(viewId, onClickListener))
    }

    fun add(onClickListener: OnLongClickListener<DATA>) {
        holders.add(LongClickListenerHolder(onClickListener))
    }

    class ClickListenerHolder<DATA>(
        @field:IdRes @param:IdRes @get:IdRes val viewId: Int,
        val listener: OnClickListener<DATA>
    ) {
        constructor(listener: OnClickListener<DATA>) : this(0, listener)
    }

    class LongClickListenerHolder<DATA>(
        @field:IdRes @param:IdRes @get:IdRes val viewId: Int,
        val listener: OnLongClickListener<DATA>
    ) {
        constructor(listener: OnLongClickListener<DATA>) : this(0, listener)
    }
}