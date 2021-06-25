package com.github.panpf.assemblyadapter.sample.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.list.concat.expandable.ConcatExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.AssemblyExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.AssemblySingleDataExpandableListAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentExpandableListBinding
import com.github.panpf.assemblyadapter.sample.item.AppGroupItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinGroupAppsViewModel

class ExpandableListFragment : BaseBindingFragment<FragmentExpandableListBinding>() {

    private val viewModel by viewModels<PinyinGroupAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentExpandableListBinding {
        return FragmentExpandableListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentExpandableListBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataExpandableListAdapter<AppsOverview, Any>(
            AppsOverviewItemFactory(requireActivity())
        )
        val listAdapter = AssemblyExpandableListAdapter<AppGroup, AppInfo>(
            listOf(AppGroupItemFactory(), AppItemFactory(requireActivity()))
        )
        val footerLoadStateAdapter = AssemblySingleDataExpandableListAdapter<LoadState, Any>(
            LoadStateItemFactory(requireActivity())
        )
        binding.expandableListList.setAdapter(
            ConcatExpandableListAdapter(
                appsOverviewAdapter,
                listAdapter,
                footerLoadStateAdapter
            )
        )

        binding.expandableListRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.expandableListRefreshLayout.isRefreshing = it == true
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
            listAdapter.setDataList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
    }
}