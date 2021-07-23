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
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.panpf.assemblyadapter.internal.ItemDataStorage

/**
 * An implementation of [FragmentStateAdapter], The data is provided by the [Fragment] list passed in from the outside.
 *
 * Warning: The [createFragment] method will not directly return the [Fragment] from [fragmentList], but uses it as a template to create a new Fragment
 */
open class ArrayFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    templateFragmentList: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val itemDataStorage = ItemDataStorage(templateFragmentList) { notifyDataSetChanged() }

    /**
     * Get the current list. If a null list is submitted through [submitFragmentList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitFragmentList].
     */
    val fragmentList: List<Fragment>
        get() = itemDataStorage.readOnlyDataList

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [ArrayFragmentStateAdapter]
     */
    constructor(
        fragmentActivity: FragmentActivity, fragments: List<Fragment>
    ) : this(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle, fragments)

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [ArrayFragmentStateAdapter]
     */
    constructor(
        fragment: Fragment, fragments: List<Fragment>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, fragments)


    /**
     * Set the new list to be displayed.
     */
    open fun submitFragmentList(fragmentList: List<Fragment>?) {
        itemDataStorage.submitDataList(fragmentList)
    }


    override fun getItemCount(): Int = itemDataStorage.dataCount

    override fun createFragment(position: Int): Fragment {
        // https://developer.android.com/training/animation/vp2-migration
        // The official document clearly states that it is necessary to ensure that the new instance returned by this method each time is not reusable
        val templateFragment = itemDataStorage.getData(position)
        return templateFragment.javaClass.newInstance().apply {
            arguments = templateFragment.arguments
        }
    }
}