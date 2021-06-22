package com.github.panpf.assemblyadapter.sample.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.list.AssemblyListAdapter
import com.github.panpf.assemblyadapter.list.AssemblySingleDataListAdapter
import com.github.panpf.assemblyadapter.list.concat.ConcatListAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentListBinding
import com.github.panpf.assemblyadapter.sample.item.AppItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.item.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel

class ListFragment : BaseBindingFragment<FragmentListBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentListBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataListAdapter(AppsOverviewItemFactory())
        val listAdapter = AssemblyListAdapter<Any>(
            listOf(AppItemFactory(), PinyinGroupItemFactory())
        )
        val loadFooterAdapter = AssemblySingleDataListAdapter(LoadStateItemFactory())
        binding.listList.adapter =
            ConcatListAdapter(appsOverviewAdapter, listAdapter, loadFooterAdapter)

        binding.listRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            listAdapter.setDataList(it)
            loadFooterAdapter.data = LoadState.NotLoading(true)
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.listRefreshLayout.isRefreshing = it == true
        }
    }
}