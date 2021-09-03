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
package com.github.panpf.assemblyadapter.pager.refreshable

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.R

/**
 * Realize that [PagerAdapter] can refresh correctly when calling notifyDataSetChanged
 */
internal class PagerAdapterRefreshHelper<DATA>(
    val getCount: () -> Int,
    val getData: (position: Int) -> Any?
) {

    constructor(pagerAdapter: GetItemDataPagerAdapter<DATA>) : this(
        { pagerAdapter.count },
        { pagerAdapter.getItemData(it) }
    )

    constructor(pagerAdapter: GetItemDataFragmentStatePagerAdapter<DATA>) : this(
        { pagerAdapter.count },
        { pagerAdapter.getItemData(it) }
    )

    fun bindPositionAndData(item: View, position: Int, data: DATA) {
        item.apply {
            setTag(R.id.aa_tag_pager_refresh_position, position)
            setTag(R.id.aa_tag_pager_refresh_data, data)
        }
    }

    fun bindPositionAndData(item: Fragment, position: Int, data: DATA) {
        item.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (source.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    source.lifecycle.removeObserver(this)

                    item.view?.apply {
                        setTag(R.id.aa_tag_pager_refresh_position, position)
                        setTag(R.id.aa_tag_pager_refresh_data, data)
                    }
                }
            }
        })
    }

    fun isItemPositionChanged(item: View): Boolean {
        val position = item.getTag(R.id.aa_tag_pager_refresh_position) as Int? ?: return true
        val data = item.getTag(R.id.aa_tag_pager_refresh_data) ?: return true
        val newData = if (position >= 0 && position < getCount()) getData(position) else null
        return newData == null || data != newData
    }

    fun isItemPositionChanged(item: Fragment): Boolean {
        val view = item.view ?: return true
        return isItemPositionChanged(view)
    }
}