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
package com.github.panpf.assemblyadapter.internal

import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import java.util.*

class ClickListenerStorage<DATA> {

    val holders: MutableList<Any> = LinkedList()

    fun add(@IdRes viewId: Int, onClickListener: OnClickListener<DATA>) {
        holders.add(ClickListenerHolder(viewId, onClickListener))
    }

    fun add(onClickListener: OnClickListener<DATA>) {
        holders.add(ClickListenerHolder(-1, onClickListener))
    }

    fun add(@IdRes viewId: Int, onClickListener: OnLongClickListener<DATA>) {
        holders.add(LongClickListenerHolder(viewId, onClickListener))
    }

    fun add(onClickListener: OnLongClickListener<DATA>) {
        holders.add(LongClickListenerHolder(-1, onClickListener))
    }

    class ClickListenerHolder<DATA>(
        @field:IdRes @param:IdRes @get:IdRes val viewId: Int,
        val listener: OnClickListener<DATA>
    )

    class LongClickListenerHolder<DATA>(
        @field:IdRes @param:IdRes @get:IdRes val viewId: Int,
        val listener: OnLongClickListener<DATA>
    )
}