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
package com.github.panpf.assemblyadapter.pager.internal

import android.view.View
import com.github.panpf.assemblyadapter.pager.R

/**
 * Realize that PagerAdapter can refresh correctly when calling notifyDataSetChanged
 */
internal class PagerAdapterRefreshHelper {

    private var notifyDataSetChangedNumber = 0

    fun onNotifyDataSetChanged() {
        if (notifyDataSetChangedNumber == Int.MAX_VALUE) {
            notifyDataSetChangedNumber = 0
        }
        notifyDataSetChangedNumber++
    }

    fun bindNotifyDataSetChangedNumber(view: View) {
        view.setTag(R.id.aa_tag_notifyDataSetChangedNumber, notifyDataSetChangedNumber)
    }

    fun isItemPositionChanged(view: View): Boolean {
        val currentNumber = notifyDataSetChangedNumber
        val bindNumber = view.getTag(R.id.aa_tag_notifyDataSetChangedNumber)?.toString()?.toInt()
        return bindNumber != currentNumber
    }
}