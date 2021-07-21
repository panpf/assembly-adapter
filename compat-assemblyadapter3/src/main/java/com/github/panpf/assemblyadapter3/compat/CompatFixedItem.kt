package com.github.panpf.assemblyadapter3.compat

open class CompatFixedItem<DATA> {

    val itemFactory: CompatAssemblyItemFactory<DATA>
    var data: DATA? = null
        set(value) {
            field = value
            callback?.onChanged(this)
        }
    var callback: Callback? = null
    var isEnabled = true
        set(value) {
            if (field != value) {
                field = value
                callback?.onChanged(this)
            }
        }

    val isAttached: Boolean
        get() = callback != null

    constructor(itemFactory: CompatAssemblyItemFactory<DATA>, data: DATA?) {
        this.itemFactory = itemFactory
        this.data = data
    }

    constructor(itemFactory: CompatAssemblyItemFactory<DATA>) {
        this.itemFactory = itemFactory
    }

    fun interface Callback {
        fun onChanged(fixedItem: CompatFixedItem<*>)
    }
}