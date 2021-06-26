package com.github.panpf.assemblyadapter.sample.item.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.pager.BindingPagerItemFactory
import com.github.panpf.assemblyadapter.sample.databinding.FragmentLoadStateBinding

class LoadStatePagerItemFactory : BindingPagerItemFactory<LoadState, FragmentLoadStateBinding>() {

    override fun match(data: Any): Boolean {
        return data is LoadState
    }

    override fun createViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        position: Int,
        data: LoadState
    ): FragmentLoadStateBinding =
        FragmentLoadStateBinding.inflate(inflater, parent, false).apply {
            loadStateLoadingLayout.isVisible = data is LoadState.Loading
            loadStateErrorText.isVisible = data is LoadState.Error
            loadStateEndText.isVisible = data is LoadState.NotLoading && data.endOfPaginationReached
        }
}