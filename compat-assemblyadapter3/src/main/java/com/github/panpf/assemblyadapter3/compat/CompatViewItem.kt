package com.github.panpf.assemblyadapter3.compat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.github.panpf.assemblyadapter3.compat.CompatViewItem.ViewFactory

class CompatViewItem(itemView: View) : CompatAssemblyItem<Any?>(itemView) {

    override fun onSetData(position: Int, data: Any?) {

    }

    class Factory @JvmOverloads constructor(
        private val viewFactory: ViewFactory,
        private val dataClazz: Class<*>? = null
    ) : CompatAssemblyItemFactory<Any?>() {
        @JvmOverloads
        constructor(
            @LayoutRes layoutResId: Int,
            dataClazz: Class<*>? = null
        ) : this(ViewFactory { parent ->
            LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        }, dataClazz)

        @JvmOverloads
        constructor(view: View, dataClazz: Class<*>? = null) : this(ViewFactory { view }, dataClazz)

        override fun match(data: Any?): Boolean {
            return dataClazz == null || dataClazz.isInstance(data)
        }

        override fun createAssemblyItem(parent: ViewGroup): CompatViewItem {
            return CompatViewItem(viewFactory.createItemView(parent))
        }
    }

    fun interface ViewFactory {
        fun createItemView(parent: ViewGroup): View
    }
}