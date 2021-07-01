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
package com.github.panpf.assemblyadapter.sample.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.github.panpf.assemblyadapter.pager2.paging.AssemblyFragmentLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.item.pager.LoadStateFragmentItemFactory

class MyFragmentLoadStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : AssemblyFragmentLoadStateAdapter(
    fragmentManager,
    lifecycle,
    LoadStateFragmentItemFactory(),
    true
) {
    constructor(
        fragmentActivity: FragmentActivity
    ) : this(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle)

    constructor(fragment: Fragment) : this(fragment.childFragmentManager, fragment.lifecycle)
}