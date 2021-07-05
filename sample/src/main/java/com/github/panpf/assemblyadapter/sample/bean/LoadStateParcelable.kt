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
package com.github.panpf.assemblyadapter.sample.bean

import android.os.Parcelable
import androidx.paging.LoadState
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoadStateParcelable(
    val type: Int,
    val endOfPaginationReached: Boolean,
    val throwableClassName: String?
) : Parcelable {

    constructor(loadState: LoadState) : this(
        when (loadState) {
            is LoadState.NotLoading -> 0
            is LoadState.Loading -> 1
            is LoadState.Error -> -1
        },
        loadState.endOfPaginationReached,
        if (loadState is LoadState.Error) loadState.error::class.java.name else null
    )

    fun toLoadState(): LoadState = when (type) {
        0 -> LoadState.NotLoading(endOfPaginationReached)
        1 -> LoadState.Loading
        -1 -> LoadState.Error(Throwable(throwableClassName))
        else -> throw IllegalStateException("Unknown type: $type")
    }
}