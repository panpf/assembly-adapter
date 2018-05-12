@file:Suppress("RedundantVisibilityModifier", "unused", "UNCHECKED_CAST")

package me.panpf.adapter.ktx

import android.view.View
import me.panpf.adapter.AssemblyItem
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing =
        throw IllegalStateException("View ID $id for '${desc.name}' not found.")

private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?) = Lazy { t: T, desc ->
    t.finder(id) as V? ?: viewNotFound(id, desc)
}

private fun <T, V : View> optional(id: Int, finder: T.(Int) -> View?) = Lazy { t: T, _ -> t.finder(id) as V? }

private fun <T, V : View> required(ids: IntArray, finder: T.(Int) -> View?) = Lazy { t: T, desc ->
    ids.map { t.finder(it) as V? ?: viewNotFound(it, desc) }
}

private fun <T, V : View> optional(ids: IntArray, finder: T.(Int) -> View?) = Lazy { t: T, _ ->
    @Suppress("SimplifiableCallChain")
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
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}


public fun <V : View> AssemblyItem<*>.bindView(id: Int)
        : ReadOnlyProperty<AssemblyItem<*>, V> = required(id, viewFinder)

public fun <V : View> AssemblyItem<*>.bindViews(vararg id: Int)
        : ReadOnlyProperty<AssemblyItem<*>, List<V>> = required(id, viewFinder)

public fun <V : View> AssemblyItem<*>.bindOptionalView(id: Int)
        : ReadOnlyProperty<AssemblyItem<*>, V?> = optional(id, viewFinder)

public fun <V : View> AssemblyItem<*>.bindOptionalViews(vararg id: Int)
        : ReadOnlyProperty<AssemblyItem<*>, List<V>> = optional(id, viewFinder)

private val AssemblyItem<*>.viewFinder: AssemblyItem<*>.(Int) -> View?
    get() = { itemView.findViewById(it) }