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
package com.github.panpf.assemblyadapter.list.expandable.test.internal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.panpf.assemblyadapter.list.expandable.SimpleExpandableChildItemFactory
import com.github.panpf.assemblyadapter.list.test.R

class StringsChildItemFactory : SimpleExpandableChildItemFactory<Strings, String>(String::class) {

    override fun createItemView(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): View = inflater.inflate(R.layout.item_test, parent, false)

    override fun initItem(
        context: Context,
        itemView: View,
        item: SimpleExpandableChildItem<Strings, String>
    ) {
    }

    override fun bindItemData(
        context: Context,
        itemView: View,
        item: SimpleExpandableChildItem<Strings, String>,
        groupBindingAdapterPosition: Int,
        groupAbsoluteAdapterPosition: Int,
        groupData: Strings,
        isLastChild: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: String
    ) {
        itemView.findViewById<TextView>(R.id.testItemTitleText).text = data
    }
}