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
package com.github.panpf.assemblyadapter.pager.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.github.panpf.assemblyadapter.pager.R

/**
 * Realize that FragmentPagerAdapter can refresh correctly when calling notifyDataSetChanged
 */
internal class FragmentPagerAdapterRefreshHelper {

    private var notifyDataSetChangedNumber = 0

    fun onNotifyDataSetChanged() {
        if (notifyDataSetChangedNumber == Int.MAX_VALUE) {
            notifyDataSetChangedNumber = 0
        }
        notifyDataSetChangedNumber++
    }

    fun bindNotifyDataSetChangedNumber(item: Fragment) {
        val lifecycleEventObserver = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (source.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    item.requireView().setTag(
                        R.id.aa_tag_pagerItem_notifyDataSetChangedNumber,
                        notifyDataSetChangedNumber
                    )
                    source.lifecycle.removeObserver(this)
                }
            }
        }
        item.lifecycle.addObserver(lifecycleEventObserver)
    }

    fun isItemPositionChanged(item: Fragment): Boolean {
        val view = item.view ?: return true
        val currentNumber = notifyDataSetChangedNumber
        val bindNumber = view.getTag(R.id.aa_tag_pagerItem_notifyDataSetChangedNumber)?.toString()?.toInt()
        return bindNumber != currentNumber
    }
}