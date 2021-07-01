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
package com.github.panpf.assemblyadapter.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingPagerItemFactory<DATA, VIEW_BINDING : ViewBinding> :
    PagerItemFactory<DATA>() {

    abstract override fun matchData(data: Any): Boolean

    override fun createItemView(
        context: Context, parent: ViewGroup, position: Int, data: DATA
    ): View {
        val inflater = LayoutInflater.from(context)
        return createItemViewBinding(context, inflater, parent, position, data).root
    }

    protected abstract fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup, position: Int, data: DATA
    ): VIEW_BINDING
}