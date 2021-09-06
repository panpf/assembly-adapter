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

import androidx.fragment.app.Fragment

/**
 * Implementation of [FragmentItemFactory], only accept data of type View and return it directly as itemView
 */
open class IntactFragmentItemFactory(private val disableRecreateFragment: Boolean = false) :
    FragmentItemFactory<Fragment>(Fragment::class) {

    override fun createFragment(
        bindingAdapterPosition: Int, absoluteAdapterPosition: Int, data: Fragment
    ): Fragment = if (!disableRecreateFragment) {
        // https://developer.android.com/training/animation/vp2-migration
        // The official document clearly states that it is necessary to ensure that the new instance returned by this method each time is not reusable
        data.javaClass.newInstance().apply {
            arguments = data.arguments
        }
    } else {
        data
    }
}