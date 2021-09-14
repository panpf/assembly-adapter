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

/**
 * Single data version of [AssemblyPagerAdapter]
 *
 * @param itemFactory Can match [data]'s [PagerItemFactory]
 * @param initData Initial data
 * @see PagerItemFactory
 */
open class AssemblySingleDataPagerAdapter<DATA : Any>(
    val itemFactory: PagerItemFactory<DATA>,
    initData: DATA? = null
) : AssemblyPagerAdapter<DATA>(listOf(itemFactory)) {

    /**
     * The only data of the current adapter, [notifyDataSetChanged] will be triggered when the data changes
     */
    var data: DATA?
        set(value) {
            if (value != null) {
                super.submitList(listOf(value))
            } else {
                super.submitList(null)
            }
        }
        get() = if (itemCount > 0) getItemData(0) else null

    /**
     * Get the current page title.
     */
    var currentPageTitle: CharSequence?
        set(value) {
            if (value != null) {
                super.submitPageTitleList(listOf(value))
            } else {
                super.submitPageTitleList(null)
            }
        }
        get() = currentPageTitleList.firstOrNull()

    init {
        if (initData != null) {
            this.data = initData
        }
    }

    override fun submitList(list: List<DATA>?) {
        require(list?.size ?: 0 <= 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list)
    }

    override fun submitPageTitleList(pageTitleList: List<CharSequence>?) {
        require(pageTitleList?.size ?: 0 <= 1) {
            "Cannot submit a pageTitleList with size greater than 1"
        }
        super.submitPageTitleList(pageTitleList)
    }
}