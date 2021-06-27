package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.databinding.ItemLoadStateBinding

class LoadStateItemFactory(private val activity: Activity) :
    BindingItemFactory<LoadState, ItemLoadStateBinding>() {

    override fun match(data: Any): Boolean = data is LoadState

    override fun createItemViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemLoadStateBinding = ItemLoadStateBinding.inflate(inflater, parent, false)

    override fun initItem(context: Context, binding: ItemLoadStateBinding, item: Item<LoadState>) {
        super.initItem(context, binding, item)

        binding.root.setOnLongClickListener {
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("LoadState").appendLine()
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}")
                })
            }.show()
            true
        }
    }

    override fun bindItemData(
        context: Context,
        binding: ItemLoadStateBinding,
        item: Item<LoadState>,
        bindingAdapterPosition: Int,
        data: LoadState
    ) {
        binding.loadStateItemLoadingLayout.isVisible = data is LoadState.Loading
        binding.loadStateItemErrorText.isVisible = data is LoadState.Error
        binding.loadStateItemEndText.isVisible =
            data is LoadState.NotLoading && data.endOfPaginationReached
    }
}