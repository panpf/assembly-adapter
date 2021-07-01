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
package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

class AssemblyStaggeredGridLayoutManager : StaggeredGridLayoutManager,
    FullSpanStaggeredGridLayoutManager {

    private val fullSpanItemFactoryList: List<KClass<out ItemFactory<*>>>

    constructor(
        context: Context, attrs: AttributeSet?,
        defStyleAttr: Int, defStyleRes: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<*>>>
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.fullSpanItemFactoryList = fullSpanItemFactoryList
    }

    constructor(
        spanCount: Int, orientation: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<*>>>
    ) : super(spanCount, orientation) {
        this.fullSpanItemFactoryList = fullSpanItemFactoryList
    }

    constructor(
        spanCount: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<*>>>
    ) : super(spanCount, VERTICAL) {
        this.fullSpanItemFactoryList = fullSpanItemFactoryList
    }

    override fun setFullSpan(itemView: View, itemFactory: ItemFactory<*>) {
        val layoutParams = itemView.layoutParams
        if (layoutParams is LayoutParams && fullSpanItemFactoryList.contains(itemFactory::class)) {
            layoutParams.isFullSpan = true
        }
    }
}