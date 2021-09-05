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

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.pager.refreshable.RefreshablePagerAdapter
import java.util.*

/**
 * An implementation of [PagerAdapter], The data is provided by the [View] list passed in from the outside.
 */
open class ArrayPagerAdapter(viewList: List<View>? = null) : RefreshablePagerAdapter<View>() {

    private val itemDataStorage = ItemDataStorage(viewList) { notifyDataSetChanged() }
    private var pageTitleStorage: ItemDataStorage<CharSequence>? = null

    /**
     * Get the current list. If a null list is submitted through [submitList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitList].
     */
    val currentList: List<View>
        get() = itemDataStorage.readOnlyList

    /**
     * Get the current page title list. If a null list is submitted through [submitPageTitleList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitPageTitleList].
     */
    val currentPageTitleList: List<CharSequence>
        get() = pageTitleStorage?.readOnlyList ?: Collections.emptyList()

    /**
     * Set the new list to be displayed.
     */
    open fun submitList(list: List<View>?) {
        itemDataStorage.submitList(list)
    }

    /**
     * Set the new page title list to be displayed.
     */
    open fun submitPageTitleList(pageTitleList: List<CharSequence>?) {
        (pageTitleStorage ?: ItemDataStorage<CharSequence> {
            notifyDataSetChanged()
        }.apply {
            this@ArrayPagerAdapter.pageTitleStorage = this
        }).submitList(pageTitleList)
    }

    val itemCount: Int
        get() = itemDataStorage.dataCount

    override fun getItemData(position: Int): View {
        return itemDataStorage.getData(position)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getView(container: ViewGroup, position: Int): View {
        return getItemData(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitleStorage?.getData(position)
    }
}