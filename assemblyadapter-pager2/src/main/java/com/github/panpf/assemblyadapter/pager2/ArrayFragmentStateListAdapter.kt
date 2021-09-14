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
package com.github.panpf.assemblyadapter.pager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import com.github.panpf.assemblyadapter.pager.IntactFragmentItemFactory

/**
 * An implementation of [FragmentStateListAdapter], The data is provided by the [Fragment] list passed in from the outside.
 *
 * Warning: The [createFragment] method will not directly return the [Fragment] from [currentList], but uses it as a template to create a new Fragment
 */
open class ArrayFragmentStateListAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    templateFragmentList: List<Fragment>? = null,
) : AssemblyFragmentStateListAdapter<Fragment>(
    fragmentManager,
    lifecycle,
    listOf(IntactFragmentItemFactory()),
    templateFragmentList,
    FragmentDiffCallback()
) {

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [ArrayFragmentStateListAdapter]
     */
    constructor(
        fragmentActivity: FragmentActivity, fragments: List<Fragment>? = null
    ) : this(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle, fragments)

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [ArrayFragmentStateListAdapter]
     */
    constructor(
        fragment: Fragment, fragments: List<Fragment>? = null
    ) : this(fragment.childFragmentManager, fragment.lifecycle, fragments)


    /**
     * Use [FragmentManager] and [Lifecycle] to create [ArrayFragmentStateListAdapter]
     */
    constructor(
        fragmentManager: FragmentManager, lifecycle: Lifecycle, fragments: Array<Fragment>
    ) : this(fragmentManager, lifecycle, fragments.toList())

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [ArrayFragmentStateListAdapter]
     */
    constructor(
        fragmentActivity: FragmentActivity, fragments: Array<Fragment>
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        fragments.toList()
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [ArrayFragmentStateListAdapter]
     */
    constructor(
        fragment: Fragment, fragments: Array<Fragment>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, fragments.toList())

    private class FragmentDiffCallback : DiffUtil.ItemCallback<Fragment>() {
        override fun areItemsTheSame(oldItem: Fragment, newItem: Fragment): Boolean {
            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItem: Fragment, newItem: Fragment): Boolean {
            return oldItem.arguments?.toString() == newItem.arguments?.toString()
        }
    }
}