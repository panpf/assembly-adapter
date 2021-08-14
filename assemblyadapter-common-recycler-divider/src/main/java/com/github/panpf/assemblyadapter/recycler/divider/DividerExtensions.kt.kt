/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter.recycler.divider

import androidx.recyclerview.widget.RecyclerView


/**
 * Create a [LinearDividerItemDecoration]. You can also configure divider through the [block] function
 */
fun RecyclerView.newLinearDividerItemDecoration(
    block: (LinearDividerItemDecoration.Builder.() -> Unit)? = null
): LinearDividerItemDecoration {
    return LinearDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build()
}

/**
 * Add a [LinearDividerItemDecoration] to the current [RecyclerView]. You can also configure divider through the [block] function
 * @param index Set the order of addition. -1 means add to the end
 */
fun RecyclerView.addLinearDividerItemDecoration(
    index: Int = -1,
    block: (LinearDividerItemDecoration.Builder.() -> Unit)? = null,
) {
    addItemDecoration(
        LinearDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}


/**
 * Create a [GridDividerItemDecoration]. You can also configure divider through the [block] function
 */
fun RecyclerView.newGridDividerItemDecoration(
    block: (GridDividerItemDecoration.Builder.() -> Unit)? = null
): GridDividerItemDecoration {
    return GridDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build()
}

/**
 * Add a [GridDividerItemDecoration] to the current [RecyclerView]. You can also configure divider through the [block] function
 * @param index Set the order of addition. -1 means add to the end
 */
fun RecyclerView.addGridDividerItemDecoration(
    index: Int = -1,
    block: (GridDividerItemDecoration.Builder.() -> Unit)? = null,
) {
    addItemDecoration(
        GridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}


/**
 * Create a [StaggeredGridDividerItemDecoration]. You can also configure divider through the [block] function
 */
fun RecyclerView.newStaggeredGridDividerItemDecoration(
    block: (StaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null
): StaggeredGridDividerItemDecoration {
    return StaggeredGridDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build()
}

/**
 * Add a [StaggeredGridDividerItemDecoration] to the current [RecyclerView]. You can also configure divider through the [block] function
 * @param index Set the order of addition. -1 means add to the end
 */
fun RecyclerView.addStaggeredGridDividerItemDecoration(
    index: Int = -1,
    block: (StaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null,
) {
    addItemDecoration(
        StaggeredGridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}