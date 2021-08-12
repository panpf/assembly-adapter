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
package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

/**
 * Implementation of [ItemFactory] for [ViewBinding] version. Users do not need to define Item,
 * which can greatly simplify the implementation logic of custom [ItemFactory]
 *
 * @param DATA Define the type of matching data
 * @param VIEW_BINDING Define the [ViewBinding] type of item view
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [exactMatchData] method to achieve exact matching
 */
abstract class BindingItemFactory<DATA : Any, VIEW_BINDING : ViewBinding>(
    dataClass: KClass<DATA>
) : ItemFactory<DATA>(dataClass) {

    override fun createItem(parent: ViewGroup): BindingItem<DATA, VIEW_BINDING> {
        val context = parent.context
        val binding = createItemViewBinding(context, LayoutInflater.from(context), parent)
        return BindingItem(this, binding)
    }

    /**
     * Create the ViewBinding of the item view
     */
    protected abstract fun createItemViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): VIEW_BINDING

    /**
     * Initialize the item, this method is only executed once when the item is created
     */
    protected abstract fun initItem(
        context: Context,
        binding: VIEW_BINDING,
        item: BindingItem<DATA, VIEW_BINDING>
    )

    /**
     * Binding item data, this method will be executed frequently
     *
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     */
    protected abstract fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: BindingItem<DATA, VIEW_BINDING>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    )

    class BindingItem<DATA : Any, VIEW_BINDING : ViewBinding>(
        val factory: BindingItemFactory<DATA, VIEW_BINDING>,
        val binding: VIEW_BINDING
    ) : Item<DATA>(binding.root) {

        init {
            factory.initItem(binding.root.context, binding, this)
        }

        override fun bindData(
            bindingAdapterPosition: Int, absoluteAdapterPosition: Int, data: DATA
        ) {
            factory.bindItemData(
                context, binding, this, bindingAdapterPosition, absoluteAdapterPosition, data
            )
        }
    }
}