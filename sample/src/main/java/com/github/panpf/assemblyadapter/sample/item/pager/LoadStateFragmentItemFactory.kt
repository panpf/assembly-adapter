package com.github.panpf.assemblyadapter.sample.item.pager

import androidx.fragment.app.Fragment
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.pager.fragment.AssemblyFragmentItemFactory

class LoadStateFragmentItemFactory : AssemblyFragmentItemFactory<LoadState>() {

    override fun match(data: Any): Boolean {
        return data is LoadState
    }

    override fun createFragment(position: Int, data: LoadState): Fragment {
        return LoadStateFragment.createInstance(data)
    }
}