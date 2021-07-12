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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

open class ViewFragmentItemFactory<DATA : Any>(
    dataClass: KClass<DATA>,
    @LayoutRes private val layoutResId: Int
) : FragmentItemFactory<DATA>(dataClass) {

    override fun createFragment(
        bindingAdapterPosition: Int, absoluteAdapterPosition: Int, data: DATA
    ): Fragment {
        return ViewFragment.createInstance(layoutResId)
    }

    class ViewFragment : Fragment() {
        companion object {
            fun createInstance(@LayoutRes layoutResId: Int) = ViewFragment().apply {
                arguments = Bundle().apply { putInt("layoutResId", layoutResId) }
            }
        }

        private val layoutResId by lazy { arguments?.getInt("layoutResId")!! }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View = inflater.inflate(layoutResId, container, false)
    }
}