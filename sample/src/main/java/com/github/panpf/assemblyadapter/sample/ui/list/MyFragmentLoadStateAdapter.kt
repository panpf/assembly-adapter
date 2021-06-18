package com.github.panpf.assemblyadapter.sample.ui.list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.github.panpf.assemblyadapter.pager2.paging.AssemblyFragmentLoadStateAdapter

class MyFragmentLoadStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : AssemblyFragmentLoadStateAdapter(
    fragmentManager,
    lifecycle,
    FragmentLoadStateItemFactory(),
    true
) {
    constructor(
        fragmentActivity: FragmentActivity
    ) : this(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle)

    constructor(fragment: Fragment) : this(fragment.childFragmentManager, fragment.lifecycle)
}