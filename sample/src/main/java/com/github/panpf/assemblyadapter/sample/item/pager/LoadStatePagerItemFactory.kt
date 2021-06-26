package com.github.panpf.assemblyadapter.sample.item.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.pager.AssemblyPagerItemFactory
import com.github.panpf.assemblyadapter.sample.databinding.FragmentLoadStateBinding

class LoadStatePagerItemFactory : AssemblyPagerItemFactory<LoadState>() {

    override fun match(data: Any): Boolean {
        return data is LoadState
    }

    override fun createView(
        context: Context, container: ViewGroup, position: Int, data: LoadState
    ): View =
        FragmentLoadStateBinding.inflate(LayoutInflater.from(context), container, false).apply {
            loadStateLoadingLayout.isVisible = data is LoadState.Loading
            loadStateErrorText.isVisible = data is LoadState.Error
            loadStateEndText.isVisible = data is LoadState.NotLoading && data.endOfPaginationReached
        }.root
}