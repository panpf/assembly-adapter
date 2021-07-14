package com.github.panpf.assemblyadapter3.compat.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter3.compat.CompatDiffableDiffCallback
import com.github.panpf.assemblyadapter3.compat.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter3.compat.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter3.compat.sample.item.AppItem
import com.github.panpf.assemblyadapter3.compat.sample.item.ListSeparatorItem
import com.github.panpf.assemblyadapter3.compat.sample.item.LoadMoreItem
import com.github.panpf.assemblyadapter3.compat.sample.item.TextItem
import com.github.panpf.assemblyadapter3.compat.sample.vm.PagingViewModel

class RecyclerPagingFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<PagingViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val pagingDataAdapter =
            CompatAssemblyPagingDataAdapter(CompatDiffableDiffCallback()).apply {
                addHeaderItem(
                    TextItem.Factory().setOnItemClickListener { _, _, _, _, _ ->
                        setHeaderItemEnabled(0, false)
                    },
                    "我是小额头呀！(我会一点就消失术)"
                )
                addItemFactory(AppItem.Factory())
                addItemFactory(ListSeparatorItem.Factory())
                addFooterItem(
                    TextItem.Factory().setOnItemClickListener { _, _, _, _, _ ->
                        setFooterItemEnabled(0, false)
                    },
                    "我是小尾巴呀！(我也会一点就消失术)"
                )
                setMoreItem(LoadMoreItem.Factory())
            }

        binding.recyclerRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = pagingDataAdapter
        }

        binding.recyclerRefreshLayout.setOnRefreshListener {
            pagingDataAdapter.setHeaderItemEnabled(0, true)
            pagingDataAdapter.setFooterItemEnabled(0, true)
            pagingDataAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingDataFlow.collect {
                pagingDataAdapter.submitData(it)
            }
        }

        pagingDataAdapter.addLoadStateListener {
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

            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.recyclerRefreshLayout.isRefreshing = true
                }
                is LoadState.NotLoading -> {
                    binding.recyclerRefreshLayout.isRefreshing = false
                }
            }
        }
    }
}