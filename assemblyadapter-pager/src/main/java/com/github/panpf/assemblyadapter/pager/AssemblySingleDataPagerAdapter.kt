/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
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

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter

class AssemblySingleDataPagerAdapter<DATA> @JvmOverloads constructor(
    private val itemFactory: AssemblyPagerItemFactory<DATA>,
    initData: DATA? = null
) : PagerAdapter(), AssemblyAdapter {

    var data: DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val itemPositionChangedHelper = PagerAdapterItemPositionChangedHelper()

    var isEnabledPositionNoneOnNotifyDataSetChanged: Boolean
        get() = itemPositionChangedHelper.isEnabledPositionNoneOnNotifyDataSetChanged
        set(enabled) {
            itemPositionChangedHelper.isEnabledPositionNoneOnNotifyDataSetChanged = enabled
        }

    override fun getCount(): Int = if (data != null) 1 else 0

    override fun isViewFromObject(view: View, item: Any): Boolean = view === item

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val data = data

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactory as AssemblyPagerItemFactory<Any>
        val itemView = itemFactory.dispatchCreateView(container.context, container, position, data)
        container.addView(itemView)
        return itemView
    }

    override fun notifyDataSetChanged() {
        itemPositionChangedHelper.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        return if (itemPositionChangedHelper.isItemPositionChanged(item)) {
            PagerAdapter.POSITION_NONE
        } else {
            super.getItemPosition(item)
        }
    }


    override fun getItemFactoryByPosition(position: Int): AssemblyPagerItemFactory<*> = itemFactory
}