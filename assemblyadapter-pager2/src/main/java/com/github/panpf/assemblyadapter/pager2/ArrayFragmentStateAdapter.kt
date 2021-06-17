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

class ArrayFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    fragments: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var fragmentList = fragments.toList()

    constructor(
        fragmentActivity: FragmentActivity, fragments: List<Fragment>
    ) : this(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle, fragments)

    constructor(
        fragment: Fragment, fragments: List<Fragment>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, fragments)


    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        // https://developer.android.com/training/animation/vp2-migration
        // The official document clearly states that it is necessary to ensure that the new instance returned by this method each time is not reusable
        val templateFragment = fragmentList[position]
        return templateFragment::class.java.newInstance().apply {
            arguments = templateFragment.arguments
        }
    }


    fun getFragmentsSnapshot(): List<Fragment> {
        return fragmentList.toList()
    }

    fun setFragments(fragments: List<Fragment>?) {
        fragmentList = fragments?.toList() ?: emptyList()
        notifyDataSetChanged()
    }
}