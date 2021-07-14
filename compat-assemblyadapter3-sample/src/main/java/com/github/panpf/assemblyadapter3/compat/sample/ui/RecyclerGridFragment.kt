package com.github.panpf.assemblyadapter3.compat.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.tools4a.dimen.ktx.dp2px
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyGridLayoutManager
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter3.compat.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter3.compat.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter3.compat.sample.item.AppGridItem
import com.github.panpf.assemblyadapter3.compat.sample.item.ListSeparatorItem
import com.github.panpf.assemblyadapter3.compat.sample.item.LoadMoreItem
import com.github.panpf.assemblyadapter3.compat.sample.item.TextItem
import com.github.panpf.assemblyadapter3.compat.sample.vm.RecyclerLinearViewModel

class RecyclerGridFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<RecyclerLinearViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val appAdapter = CompatAssemblyRecyclerAdapter().apply {
            addHeaderItem(
                TextItem.Factory().fullSpan().setOnItemClickListener { _, _, _, _, _ ->
                    setHeaderItemEnabled(0, false)
                },
                "我是小额头呀！(我会一点就消失术)"
            ).apply { isEnabled = false }
            addItemFactory(AppGridItem.Factory())
            addItemFactory(ListSeparatorItem.Factory().fullSpan())
            addFooterItem(
                TextItem.Factory().fullSpan().setOnItemClickListener { _, _, _, _, _ ->
                    setFooterItemEnabled(0, false)
                },
                "我是小尾巴呀！(我也会一点就消失术)"
            ).apply { isEnabled = false }
            setMoreItem(LoadMoreItem.Factory {
                viewModel.apppend()
            }.fullSpan())
        }
        binding.recyclerRecycler.apply {
            layoutManager = CompatAssemblyGridLayoutManager(context, 3)
            adapter = appAdapter
            addItemDecoration(
                context.dividerBuilder().asSpace()
                    .showSideDividers().showLastDivider()
                    .size(20.dp2px).build()
            )
        }

        binding.recyclerStickyContainer.updatePadding(left = 20.dp2px, right = 20.dp2px)

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