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
package com.github.panpf.assemblyadapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

/**
 * The [View] version of [ExpandableGroupItemFactory]. Create [ViewExpandableGroupItemFactory] directly and provide a [View] or layout id, you can use it
 *
 * @param DATA Define the type of matching data
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [exactMatchData] method to achieve exact matching
 * @param viewFactory Responsible for providing View when creating Item
 */
open class ViewExpandableGroupItemFactory<DATA : ExpandableGroup>(
    dataClass: KClass<DATA>,
    private val viewFactory: (context: Context, inflater: LayoutInflater, parent: ViewGroup) -> View
) : SimpleExpandableGroupItemFactory<DATA>(dataClass) {

    constructor(dataClazz: KClass<DATA>, @LayoutRes layoutResId: Int) : this(
        dataClazz,
        { _, inflater, parent -> inflater.inflate(layoutResId, parent, false) }
    )

    constructor(dataClazz: KClass<DATA>, view: View) : this(dataClazz, { _, _, _ -> view })

    override fun createItemView(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): View {
        return viewFactory(context, inflater, parent)
    }

    override fun bindItemData(
        context: Context,
        itemView: View,
        item: ExpandableGroupItem<DATA>,
        isExpanded: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA,
    ) {
    }
}