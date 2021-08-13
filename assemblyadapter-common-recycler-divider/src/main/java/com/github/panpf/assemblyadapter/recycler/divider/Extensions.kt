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

import android.content.Context
import androidx.recyclerview.widget.RecyclerView


fun Context.newLinearDividerItemDecorationBuilder(): LinearDividerItemDecoration.Builder {
    return LinearDividerItemDecoration.Builder(this)
}

fun RecyclerView.newLinearDividerItemDecorationBuilder(): LinearDividerItemDecoration.Builder {
    return LinearDividerItemDecoration.Builder(context)
}

fun RecyclerView.addLinearDividerItemDecoration(block: (LinearDividerItemDecoration.Builder.() -> Unit)? = null) {
    addItemDecoration(LinearDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build())
}

fun RecyclerView.addLinearDividerItemDecoration(
    block: (LinearDividerItemDecoration.Builder.() -> Unit)? = null,
    index: Int
) {
    addItemDecoration(
        LinearDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}


fun Context.newGridDividerItemDecorationBuilder(): GridDividerItemDecoration.Builder {
    return GridDividerItemDecoration.Builder(this)
}

fun RecyclerView.newGridDividerItemDecorationBuilder(): GridDividerItemDecoration.Builder {
    return GridDividerItemDecoration.Builder(context)
}

fun RecyclerView.addGridDividerItemDecoration(block: (GridDividerItemDecoration.Builder.() -> Unit)? = null) {
    addItemDecoration(GridDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build())
}

fun RecyclerView.addGridDividerItemDecoration(
    block: (GridDividerItemDecoration.Builder.() -> Unit)? = null,
    index: Int
) {
    addItemDecoration(
        GridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}


fun Context.newStaggeredGridDividerItemDecorationBuilder(): StaggeredGridDividerItemDecoration.Builder {
    return StaggeredGridDividerItemDecoration.Builder(this)
}

fun RecyclerView.newStaggeredGridDividerItemDecorationBuilder(): StaggeredGridDividerItemDecoration.Builder {
    return StaggeredGridDividerItemDecoration.Builder(context)
}

fun RecyclerView.addStaggeredGridDividerItemDecoration(block: (StaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null) {
    addItemDecoration(StaggeredGridDividerItemDecoration.Builder(context).apply {
        block?.invoke(this)
    }.build())
}

fun RecyclerView.addStaggeredGridDividerItemDecoration(
    block: (StaggeredGridDividerItemDecoration.Builder.() -> Unit)? = null,
    index: Int
) {
    addItemDecoration(
        StaggeredGridDividerItemDecoration.Builder(context).apply {
            block?.invoke(this)
        }.build(),
        index
    )
}