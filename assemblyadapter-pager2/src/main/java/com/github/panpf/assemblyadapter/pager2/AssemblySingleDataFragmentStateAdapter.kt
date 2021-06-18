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
package com.github.panpf.assemblyadapter.pager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentItemFactory

class AssemblySingleDataFragmentStateAdapter<DATA>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val itemFactory: AssemblyFragmentItemFactory<DATA>,
    initData: DATA? = null
) : FragmentStateAdapter(fragmentManager, lifecycle), AssemblyAdapter {

    var data: DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    constructor(
        fragmentActivity: FragmentActivity,
        itemFactory: AssemblyFragmentItemFactory<DATA>,
        initData: DATA? = null
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactory,
        initData
    )

    constructor(
        fragment: Fragment,
        itemFactory: AssemblyFragmentItemFactory<DATA>,
        initData: DATA? = null
    ) : this(fragment.childFragmentManager, fragment.lifecycle, itemFactory, initData)

    override fun getItemCount(): Int = if (data != null) 1 else 0

    override fun createFragment(position: Int): Fragment {
        return itemFactory.dispatchCreateFragment(position, data)
    }


    override fun getItemFactoryByPosition(position: Int): AssemblyFragmentItemFactory<*> {
        return itemFactory
    }
}