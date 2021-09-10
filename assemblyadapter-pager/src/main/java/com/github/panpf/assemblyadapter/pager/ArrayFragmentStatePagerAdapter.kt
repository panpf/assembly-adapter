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

import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * An implementation of [FragmentStatePagerAdapter], The data is provided by the [Fragment] list passed in from the outside.
 *
 * Warning: The [getItem] method will not directly return the [Fragment] from [currentList], but uses it as a template to create a new Fragment
 */
@Deprecated(
    message = "Switch to 'androidx.viewpager2.widget.ViewPager2' and use 'com.github.panpf.assemblyadapter.pager2.ArrayFragmentStateAdapter' instead.",
    replaceWith = ReplaceWith(
        "ArrayFragmentStateAdapter",
        "com.github.panpf.assemblyadapter.pager2.ArrayFragmentStateAdapter"
    )
)
open class ArrayFragmentStatePagerAdapter(
    fragmentManager: FragmentManager,
    @Behavior behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT,
    templateFragmentList: List<Fragment>? = null
) : AssemblyFragmentStatePagerAdapter<Fragment>(
    fragmentManager,
    behavior,
    listOf(IntactFragmentItemFactory()),
    templateFragmentList
) {

    @Deprecated(
        """use {@link #FragmentArrayStatePagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(fm: FragmentManager, fragments: List<Fragment>? = null) : this(
        fm, BEHAVIOR_SET_USER_VISIBLE_HINT, fragments
    )

    @Deprecated(
        """use {@link #FragmentArrayStatePagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fragmentManager: FragmentManager,
        @Behavior behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT, fragments: Array<Fragment>
    ) : this(
        fragmentManager, behavior, fragments.toList()
    )

    @Deprecated(
        """use {@link #FragmentArrayStatePagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(fm: FragmentManager, fragments: Array<Fragment>) : this(
        fm, BEHAVIOR_SET_USER_VISIBLE_HINT, fragments.toList()
    )

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}