package com.github.panpf.assemblyadapter3.compat.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyListAdapter
import com.github.panpf.assemblyadapter3.compat.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter3.compat.sample.databinding.FragmentListBinding
import com.github.panpf.assemblyadapter3.compat.sample.item.AppItem
import com.github.panpf.assemblyadapter3.compat.sample.item.ListSeparatorItem
import com.github.panpf.assemblyadapter3.compat.sample.item.LoadMoreItem
import com.github.panpf.assemblyadapter3.compat.sample.item.TextItem
import com.github.panpf.assemblyadapter3.compat.sample.vm.RecyclerLinearViewModel

class ListFragment : BaseBindingFragment<FragmentListBinding>() {

    private val viewModel by viewModels<RecyclerLinearViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentListBinding, savedInstanceState: Bundle?) {
        val appAdapter = CompatAssemblyListAdapter().apply {
            addHeaderItem(
                TextItem.Factory().setOnItemClickListener { _, _, _, _, _ ->
                    setHeaderItemEnabled(0, false)
                },
                "我是小额头呀！(我会一点就消失术)"
            ).apply { isEnabled = false }
            addItemFactory(AppItem.Factory())
            addItemFactory(ListSeparatorItem.Factory())
            addFooterItem(
                TextItem.Factory().setOnItemClickListener { _, _, _, _, _ ->
                    setFooterItemEnabled(0, false)
                },
                "我是小尾巴呀！(我也会一点就消失术)"
            ).apply { isEnabled = false }
            setMoreItem(LoadMoreItem.Factory {
                viewModel.apppend()
            })
        }
        binding.listList.adapter = appAdapter

        binding.listRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshAppListData.observe(viewLifecycleOwner) {
            appAdapter.setHeaderItemEnabled(0, true)
            appAdapter.setFooterItemEnabled(0, true)
            appAdapter.dataList = it
        }

        viewModel.appendAppListData.observe(viewLifecycleOwner) {
            appAdapter.addAll(it)
            appAdapter.loadMoreFinished(it.size < viewModel.size)
        }

        viewModel.refreshingData.observe(viewLifecycleOwner) {
            binding.listRefreshLayout.isRefreshing = it == true
        }
    }
}
