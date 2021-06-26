package com.github.panpf.assemblyadapter.pager2.paging

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.pager.fragment.FragmentItemFactory

open class AssemblyFragmentLoadStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val itemFactory: FragmentItemFactory<LoadState>,
    private val alwaysShowWhenEndOfPaginationReached: Boolean = false,
) : FragmentLoadStateAdapter(
    fragmentManager,
    lifecycle
) {
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactory: FragmentItemFactory<LoadState>
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactory
    )

    constructor(
        fragment: Fragment,
        itemFactory: FragmentItemFactory<LoadState>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, itemFactory)

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading
                || loadState is LoadState.Error
                || (alwaysShowWhenEndOfPaginationReached && loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }

    override fun onCreateFragment(position: Int, loadState: LoadState): Fragment {
        return itemFactory.dispatchCreateFragment(position, loadState)
    }
}