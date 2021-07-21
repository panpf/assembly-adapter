package com.github.panpf.assemblyadapter3.compat.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter3.compat.sample.R
import com.github.panpf.assemblyadapter3.compat.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter3.compat.sample.base.sticky.CompatAssemblyStickyRecyclerItemDecoration
import com.github.panpf.assemblyadapter3.compat.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter3.compat.sample.item.AppItem
import com.github.panpf.assemblyadapter3.compat.sample.item.ListSeparatorItem
import com.github.panpf.assemblyadapter3.compat.sample.item.LoadMoreItem
import com.github.panpf.assemblyadapter3.compat.sample.item.TextItem
import com.github.panpf.assemblyadapter3.compat.sample.vm.RecyclerLinearViewModel

class RecyclerLinearFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<RecyclerLinearViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val appAdapter = CompatAssemblyRecyclerAdapter().apply {
            addHeaderItem(
                TextItem.Factory().setOnItemClickListener { _, _, _, _, _ ->
                    setHeaderItemEnabled(0, false)
                },
                requireContext().getString(R.string.list_header)
            ).apply { isEnabled = false }
            addItemFactory(AppItem.Factory())
            addItemFactory(ListSeparatorItem.Factory())
            addFooterItem(
                TextItem.Factory().setOnItemClickListener { _, _, _, _, _ ->
                    setFooterItemEnabled(0, false)
                },
                requireContext().getString(R.string.list_footer)
            ).apply { isEnabled = false }
            setMoreItem(LoadMoreItem.Factory {
                viewModel.apppend()
            })
        }

        binding.recyclerRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = appAdapter
            addItemDecoration(
                CompatAssemblyStickyRecyclerItemDecoration(
                    binding.recyclerStickyContainer,
                    listOf(ListSeparatorItem.Factory::class)
                )
            )
        }

        binding.recyclerRefreshLayout.setOnRefreshListener {
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
            binding.recyclerRefreshLayout.isRefreshing = it == true
        }
    }
}