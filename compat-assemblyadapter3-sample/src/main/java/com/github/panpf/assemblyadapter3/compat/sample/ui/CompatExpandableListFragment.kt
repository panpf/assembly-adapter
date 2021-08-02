package com.github.panpf.assemblyadapter3.compat.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyExpandableAdapter
import com.github.panpf.assemblyadapter3.compat.sample.R
import com.github.panpf.assemblyadapter3.compat.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter3.compat.sample.databinding.FragmentExpandableListBinding
import com.github.panpf.assemblyadapter3.compat.sample.item.AppGroupItem
import com.github.panpf.assemblyadapter3.compat.sample.item.AppItem
import com.github.panpf.assemblyadapter3.compat.sample.item.LoadMoreItem
import com.github.panpf.assemblyadapter3.compat.sample.item.TextItem
import com.github.panpf.assemblyadapter3.compat.sample.vm.ExpandableListViewModel

class CompatExpandableListFragment : BaseBindingFragment<FragmentExpandableListBinding>() {

    private val viewModel by viewModels<ExpandableListViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentExpandableListBinding {
        return FragmentExpandableListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentExpandableListBinding, savedInstanceState: Bundle?) {
        val appAdapter = CompatAssemblyExpandableAdapter().apply {
            addHeaderItem(
                TextItem.Factory().setOnItemClickListener { _, _, _, _, _ ->
                    setHeaderItemEnabled(0, false)
                },
                requireContext().getString(R.string.list_header)
            ).apply { isEnabled = false }
            addGroupItemFactory(AppGroupItem.Factory())
            addChildItemFactory(AppItem.Factory())
            setMoreItem(LoadMoreItem.Factory {
                viewModel.apppend()
            })
            addFooterItem(
                TextItem.Factory().setOnItemClickListener { _, _, _, _, _ ->
                    setFooterItemEnabled(0, false)
                },
                requireContext().getString(R.string.list_footer)
            ).apply { isEnabled = false }
        }

        binding.expandableListList.setAdapter(appAdapter)

        binding.expandableListRefreshLayout.setOnRefreshListener {
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
            binding.expandableListRefreshLayout.isRefreshing = it == true
        }
    }
}
