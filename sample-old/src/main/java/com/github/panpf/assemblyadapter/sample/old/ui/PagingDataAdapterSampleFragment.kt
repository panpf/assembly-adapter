package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.databinding.FmRecyclerBinding
import com.github.panpf.assemblyadapter.sample.old.ui.list.GameItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.LoadMoreItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.TextItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.UserItem
import com.github.panpf.assemblyadapter.sample.old.vm.PagingDataAdapterSampleViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.panpf.adapter.FixedItem
import me.panpf.adapter.paged.DiffableDiffCallback
import me.panpf.adapter.paging.AssemblyPagingDataAdapter

class PagingDataAdapterSampleFragment : BaseBindingFragment<FmRecyclerBinding>() {

    private val viewModel by viewModels<PagingDataAdapterSampleViewModel>()

    private var headerItem: FixedItem<String>? = null
    private var footerItem: FixedItem<String>? = null

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FmRecyclerBinding {
        return FmRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmRecyclerBinding, savedInstanceState: Bundle?) {
        val pagingDataAdapter = AssemblyPagingDataAdapter<Any>(DiffableDiffCallback()).apply {
            headerItem = addHeaderItem(
                TextItem.Factory()
                    .setOnItemClickListener { context, view, position, positionInPart, data ->
                        headerItem?.setEnabled(false)
                    }, "我是小额头呀！"
            )
            addItemFactory(UserItem.Factory())
            addItemFactory(GameItem.Factory())
            footerItem = addFooterItem(
                TextItem.Factory()
                    .setOnItemClickListener { context, view, position, positionInPart, data ->
                        footerItem?.setEnabled(false)
                    }, "我是小尾巴呀！"
            )
            setMoreItem(LoadMoreItem.Factory())
        }

        binding.recyclerFmRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = pagingDataAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingDataFlow.collect {
                pagingDataAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataAdapter.loadStateFlow.collect {
                when (it.append) {
                    is LoadState.NotLoading -> {
                        pagingDataAdapter.loadMoreFinished(it.append.endOfPaginationReached)
                    }
                    is LoadState.Error -> {
                        pagingDataAdapter.loadMoreFailed()
                    }
                    is LoadState.Loading -> {
                        pagingDataAdapter.loadMoreFinished(false)
                    }
                }
            }
        }
    }
}