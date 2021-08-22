package com.github.panpf.assemblyadapter3.compat.ktx

import android.view.View
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItem
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing =
    throw IllegalStateException("View ID $id for '${desc.name}' not found.")

private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?) = Lazy { t: T, desc ->
    t.finder(id) as V? ?: viewNotFound(id, desc)
}

private fun <T, V : View> optional(id: Int, finder: T.(Int) -> View?) =
    Lazy { t: T, _ -> t.finder(id) as V? }

private fun <T, V : View> required(ids: IntArray, finder: T.(Int) -> View?) = Lazy { t: T, desc ->
    ids.map { t.finder(it) as V? ?: viewNotFound(it, desc) }
}

private fun <T, V : View> optional(ids: IntArray, finder: T.(Int) -> View?) = Lazy { t: T, _ ->
    ids.map { t.finder(it) as V? }.filterNotNull()
}

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        return value as V
    }
}


public fun <V : View> CompatAssemblyItem<*>.bindView(id: Int)
        : ReadOnlyProperty<CompatAssemblyItem<*>, V> = required(id, viewFinder)

public fun <V : View> CompatAssemblyItem<*>.bindViews(vararg id: Int)
        : ReadOnlyProperty<CompatAssemblyItem<*>, List<V>> = required(id, viewFinder)

public fun <V : View> CompatAssemblyItem<*>.bindOptionalView(id: Int)
        : ReadOnlyProperty<CompatAssemblyItem<*>, V?> = optional(id, viewFinder)

public fun <V : View> CompatAssemblyItem<*>.bindOptionalViews(vararg id: Int)
        : ReadOnlyProperty<CompatAssemblyItem<*>, List<V>> = optional(id, viewFinder)

private val CompatAssemblyItem<*>.viewFinder: CompatAssemblyItem<*>.(Int) -> View?
    get() = { itemView.findViewById(it) }