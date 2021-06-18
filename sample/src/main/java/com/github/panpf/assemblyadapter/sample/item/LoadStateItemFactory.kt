package com.github.panpf.assemblyadapter.sample.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.databinding.ItemLoadStateBinding

class LoadStateItemFactory : BindingAssemblyItemFactory<LoadState, ItemLoadStateBinding>() {

    override fun match(data: Any?): Boolean = data is LoadState

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemLoadStateBinding = ItemLoadStateBinding.inflate(inflater, parent, false)

    override fun bindData(
        context: Context,
        binding: ItemLoadStateBinding,
        item: BindingAssemblyItem<LoadState, ItemLoadStateBinding>,
        position: Int,
        data: LoadState?
    ) {
        binding.loadStateItemLoadingLayout.isVisible = data is LoadState.Loading
        binding.loadStateItemErrorText.isVisible = data is LoadState.Error
        binding.loadStateItemEndText.isVisible = data is LoadState.NotLoading && data.endOfPaginationReached
    }
}