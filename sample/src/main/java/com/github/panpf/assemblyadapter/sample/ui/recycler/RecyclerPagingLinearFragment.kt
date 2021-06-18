package com.github.panpf.assemblyadapter.sample.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter.recycler.paging.KeyDiffItemCallback
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.item.AppItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.base.MyLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.item.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatPagingAppsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecyclerPagingLinearFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<PinyinFlatPagingAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataRecyclerAdapter<AppsOverview>(
            AppsOverviewItemFactory()
        )
        val pagingDataAdapter = AssemblyPagingDataAdapter(
            listOf(AppItemFactory(), PinyinGroupItemFactory()),
            KeyDiffItemCallback()
        )
        binding.recyclerRecycler.apply {
            adapter = ConcatAdapter(
                appsOverviewAdapter,
                pagingDataAdapter.withLoadStateFooter(MyLoadStateAdapter())
            )
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.recyclerRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            pagingDataAdapter.refresh()
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pinyinFlatAppListDataFlow.collect {
                pagingDataAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataAdapter.loadStateFlow.collect {
                binding.recyclerRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }
    }
}