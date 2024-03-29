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
package com.github.panpf.assemblyadapter.recycler.divider.internal

import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper
import com.github.panpf.assemblyadapter.recycler.divider.FindItemFactoryClassSupport

class ConcatFindItemFactoryClassSupport(
    private val findItemFactoryClassSupport: FindItemFactoryClassSupport
) : FindItemFactoryClassSupport {

    private val concatAdapterLocalHelper by lazy { ConcatAdapterLocalHelper() }

    override fun findItemFactoryClassByPosition(
        adapter: RecyclerView.Adapter<*>,
        position: Int
    ): Class<*>? {
        val (localAdapter, localPosition) = concatAdapterLocalHelper
            .findLocalAdapterAndPosition(adapter, position)
        return findItemFactoryClassSupport.findItemFactoryClassByPosition(
            localAdapter,
            localPosition
        )
    }
}