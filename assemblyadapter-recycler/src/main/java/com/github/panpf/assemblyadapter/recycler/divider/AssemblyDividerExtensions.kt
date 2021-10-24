package com.github.panpf.assemblyadapter.recycler.divider

import android.content.Context
import androidx.recyclerview.widget.RecyclerView


/**
 * Create a [AssemblyLinearDividerItemDecoration]. You can also configure divider through the [block] function
 */
fun Context.newAssemblyLinearDividerItemDecoration(
    block: (AssemblyLinearDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyLinearDividerItemDecoration {
    return AssemblyLinearDividerItemDecoration.Builder(this).apply {
        block?.invoke(this)
    }.build()
}

/**
 * Create a [AssemblyLinearDividerItemDecoration]. You can also configure divider through the [block] function
 */
fun RecyclerView.newAssemblyLinearDividerItemDecoration(
    block: (AssemblyLinearDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyLinearDividerItemDecoration {
    return AssemblyLinearDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build()
}

/**
 * Add a [AssemblyLinearDividerItemDecoration] to the current [RecyclerView]. You can also configure divider through the [block] function
 * @param index Set the order of addition. -1 means add to the end
 */
fun RecyclerView.addAssemblyLinearDividerItemDecoration(
    index: Int = -1,
    block: (AssemblyLinearDividerItemDecoration.Builder.() -> Unit)? = null,
) {
    addItemDecoration(
        AssemblyLinearDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}


/**
 * Create a [AssemblyGridDividerItemDecoration]. You can also configure divider through the [block] function
 */
fun Context.newAssemblyGridDividerItemDecoration(
    block: (AssemblyGridDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyGridDividerItemDecoration {
    return AssemblyGridDividerItemDecoration.Builder(this).apply {
        block?.invoke(this)
    }.build()
}

/**
 * Create a [AssemblyGridDividerItemDecoration]. You can also configure divider through the [block] function
 */
fun RecyclerView.newAssemblyGridDividerItemDecoration(
    block: (AssemblyGridDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyGridDividerItemDecoration {
    return AssemblyGridDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build()
}

/**
 * Add a [AssemblyGridDividerItemDecoration] to the current [RecyclerView]. You can also configure divider through the [block] function
 * @param index Set the order of addition. -1 means add to the end
 */
fun RecyclerView.addAssemblyGridDividerItemDecoration(
    index: Int = -1,
    block: (AssemblyGridDividerItemDecoration.Builder.() -> Unit)? = null,
) {
    addItemDecoration(
        AssemblyGridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}


/**
 * Create a [AssemblyStaggeredGridDividerItemDecoration]. You can also configure divider through the [block] function
 */
fun Context.newAssemblyStaggeredGridDividerItemDecoration(
    block: (AssemblyStaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyStaggeredGridDividerItemDecoration {
    return AssemblyStaggeredGridDividerItemDecoration.Builder(this).apply {
        block?.invoke(this)
    }.build()
}

/**
 * Create a [AssemblyStaggeredGridDividerItemDecoration]. You can also configure divider through the [block] function
 */
fun RecyclerView.newAssemblyStaggeredGridDividerItemDecoration(
    block: (AssemblyStaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyStaggeredGridDividerItemDecoration {
    return AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build()
}

/**
 * Add a [AssemblyStaggeredGridDividerItemDecoration] to the current [RecyclerView]. You can also configure divider through the [block] function
 * @param index Set the order of addition. -1 means add to the end
 */
fun RecyclerView.addAssemblyStaggeredGridDividerItemDecoration(
    index: Int = -1,
    block: (AssemblyStaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null,
) {
    addItemDecoration(
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}