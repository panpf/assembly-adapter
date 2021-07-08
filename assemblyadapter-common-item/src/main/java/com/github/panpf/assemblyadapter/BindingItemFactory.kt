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
 * Implementation of ItemFactory for ViewBinding version. Users do not need to define Item,
 * which can greatly simplify the implementation logic of custom ItemFactory
 *
 * @param DATA Define the type of matching data
 * @param VIEW_BINDING Define the ViewBinding type of itemView
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [carefullyMatchData] method to achieve exact matching
 */
abstract class BindingItemFactory<DATA : Any, VIEW_BINDING : ViewBinding>(
    dataClass: KClass<DATA>
) : ItemFactory<DATA>(dataClass) {

    override fun createItem(parent: ViewGroup): Item<DATA> {
        val context = parent.context
        val binding = createItemViewBinding(context, LayoutInflater.from(context), parent)
        return BindingItem(this, binding).apply {
            initItem(parent.context, binding, this)
        }
    }

    /**
     * Create the ViewBinding of the itemView
     */
    protected abstract fun createItemViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): VIEW_BINDING

    /**
     * Initialize the item, this method is only executed once when the item is created
     */
    protected open fun initItem(context: Context, binding: VIEW_BINDING, item: Item<DATA>) {
    }

    /**
     * Binding item data, this method will be executed frequently
     */
    protected abstract fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: Item<DATA>,
        bindingAdapterPosition: Int,
        data: DATA
    )

    private class BindingItem<DATA : Any, VIEW_BINDING : ViewBinding>(
        private val factory: BindingItemFactory<DATA, VIEW_BINDING>,
        private val binding: VIEW_BINDING
    ) : Item<DATA>(binding.root) {

        override fun bindData(bindingAdapterPosition: Int, data: DATA) {
            factory.bindItemData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}