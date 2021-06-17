package com.github.panpf.assemblyadapter.pager2.paging

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentItemFactory

class AssemblyFragmentLoadStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val itemFactory: AssemblyFragmentItemFactory<LoadState>
) : FragmentLoadStateAdapter(
    fragmentManager,
    lifecycle
) {
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactory: AssemblyFragmentItemFactory<LoadState>
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactory
    )

    constructor(
        fragment: Fragment,
        itemFactory: AssemblyFragmentItemFactory<LoadState>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, itemFactory)

    override fun onCreateFragment(position: Int, loadState: LoadState): Fragment {
        return itemFactory.dispatchCreateFragment(position, loadState)
    }
}