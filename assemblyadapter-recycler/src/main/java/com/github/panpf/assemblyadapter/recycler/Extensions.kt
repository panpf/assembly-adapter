package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView


/************************************** Linear ****************************************************/


fun Context.newAssemblyLinearDividerItemDecoration(
    block: (AssemblyLinearDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyLinearDividerItemDecoration {
    return AssemblyLinearDividerItemDecoration.Builder(this).apply {
        block?.invoke(this)
    }.build()
}

fun RecyclerView.newAssemblyLinearDividerItemDecoration(
    block: (AssemblyLinearDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyLinearDividerItemDecoration {
    return AssemblyLinearDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build()
}


fun Context.newAssemblyLinearDividerItemDecorationBuilder(): AssemblyLinearDividerItemDecoration.Builder {
    return AssemblyLinearDividerItemDecoration.Builder(this)
}

fun RecyclerView.newAssemblyLinearDividerItemDecorationBuilder(): AssemblyLinearDividerItemDecoration.Builder {
    return AssemblyLinearDividerItemDecoration.Builder(context)
}

fun RecyclerView.addAssemblyLinearDividerItemDecoration(block: (AssemblyLinearDividerItemDecoration.Builder.() -> Unit)? = null) {
    addItemDecoration(
        AssemblyLinearDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build()
    )
}

fun RecyclerView.addAssemblyLinearDividerItemDecoration(
    block: (AssemblyLinearDividerItemDecoration.Builder.() -> Unit)? = null,
    index: Int
) {
    addItemDecoration(
        AssemblyLinearDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}


/**************************************** Grid ****************************************************/


fun Context.newAssemblyGridDividerItemDecoration(
    block: (AssemblyGridDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyGridDividerItemDecoration {
    return AssemblyGridDividerItemDecoration.Builder(this).apply {
        block?.invoke(this)
    }.build()
}

fun RecyclerView.newAssemblyGridDividerItemDecoration(
    block: (AssemblyGridDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyGridDividerItemDecoration {
    return AssemblyGridDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build()
}


fun Context.newAssemblyGridDividerItemDecorationBuilder(): AssemblyGridDividerItemDecoration.Builder {
    return AssemblyGridDividerItemDecoration.Builder(this)
}

fun RecyclerView.newAssemblyGridDividerItemDecorationBuilder(): AssemblyGridDividerItemDecoration.Builder {
    return AssemblyGridDividerItemDecoration.Builder(context)
}

fun RecyclerView.addAssemblyGridDividerItemDecoration(block: (AssemblyGridDividerItemDecoration.Builder.() -> Unit)? = null) {
    addItemDecoration(
        AssemblyGridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build()
    )
}

fun RecyclerView.addAssemblyGridDividerItemDecoration(
    block: (AssemblyGridDividerItemDecoration.Builder.() -> Unit)? = null,
    index: Int
) {
    addItemDecoration(
        AssemblyGridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}


/************************************** StaggeredGrid *********************************************/


fun Context.newAssemblyStaggeredGridDividerItemDecoration(
    block: (AssemblyStaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyStaggeredGridDividerItemDecoration {
    return AssemblyStaggeredGridDividerItemDecoration.Builder(this).apply {
        block?.invoke(this)
    }.build()
}

fun RecyclerView.newAssemblyStaggeredGridDividerItemDecoration(
    block: (AssemblyStaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null
): AssemblyStaggeredGridDividerItemDecoration {
    return AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build()
}


fun Context.newAssemblyStaggeredGridDividerItemDecorationBuilder(): AssemblyStaggeredGridDividerItemDecoration.Builder {
    return AssemblyStaggeredGridDividerItemDecoration.Builder(this)
}

fun RecyclerView.newAssemblyStaggeredGridDividerItemDecorationBuilder(): AssemblyStaggeredGridDividerItemDecoration.Builder {
    return AssemblyStaggeredGridDividerItemDecoration.Builder(context)
}

fun RecyclerView.addAssemblyStaggeredGridDividerItemDecoration(block: (AssemblyStaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null) {
    addItemDecoration(
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build()
    )
}

fun RecyclerView.addAssemblyStaggeredGridDividerItemDecoration(
    block: (AssemblyStaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null,
    index: Int
) {
    addItemDecoration(
        AssemblyStaggeredGridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}