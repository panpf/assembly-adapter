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
import kotlin.reflect.KClass

/**
 * Implementation of [PagerItemFactory] for [ViewBinding] version.
 *
 * @param DATA Define the type of matching data
 * @param VIEW_BINDING Define the [ViewBinding] type of item view
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [exactMatchData] method to achieve exact matching
 */
abstract class BindingPagerItemFactory<DATA : Any, VIEW_BINDING : ViewBinding>(
    dataClass: KClass<DATA>
) : PagerItemFactory<DATA>(dataClass) {

    override fun createItemView(
        context: Context,
        parent: ViewGroup,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ): View {
        val inflater = LayoutInflater.from(context)
        return createItemViewBinding(
            context, inflater, parent, bindingAdapterPosition, absoluteAdapterPosition, data
        ).root
    }

    /**
     * Create the ViewBinding of the item view
     */
    protected abstract fun createItemViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ): VIEW_BINDING
}