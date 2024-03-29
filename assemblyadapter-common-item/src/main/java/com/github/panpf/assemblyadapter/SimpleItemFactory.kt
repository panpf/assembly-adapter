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
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

/**
 * Simplified version of [ItemFactory]. Users do not need to define Item,
 * which can greatly simplify the implementation logic of custom [ItemFactory]
 *
 * @param DATA Define the type of matching data
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [exactMatchData] method to achieve exact matching
 */
abstract class SimpleItemFactory<DATA : Any>(
    dataClass: KClass<DATA>
) : ItemFactory<DATA>(dataClass) {

    final override fun createItem(parent: ViewGroup): SimpleItem<DATA> {
        val context = parent.context
        val itemView = createItemView(context, LayoutInflater.from(context), parent)
        return SimpleItem(this, itemView)
    }

    /**
     * Create the view required for the item
     */
    protected abstract fun createItemView(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): View

    /**
     * Initialize the item, this method is only executed once when the item is created
     */
    protected abstract fun initItem(context: Context, itemView: View, item: SimpleItem<DATA>)


    /**
     * Binding item data, this method will be executed frequently
     */
    protected abstract fun bindItemData(
        context: Context,
        itemView: View,
        item: SimpleItem<DATA>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    )

    class SimpleItem<DATA : Any>(
        val factory: SimpleItemFactory<DATA>, itemView: View
    ) : ExtraItem<DATA>(itemView) {

        init {
            factory.initItem(itemView.context, itemView, this)
        }

        override fun bindData(
            bindingAdapterPosition: Int, absoluteAdapterPosition: Int, data: DATA
        ) {
            factory.bindItemData(
                context, itemView, this, bindingAdapterPosition, absoluteAdapterPosition, data
            )
        }
    }
}