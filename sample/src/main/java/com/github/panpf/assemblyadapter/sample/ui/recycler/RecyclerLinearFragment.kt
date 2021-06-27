package com.github.panpf.assemblyadapter.sample.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.item.AppItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel

class RecyclerLinearFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter =
            AssemblySingleDataRecyclerAdapter(AppsOverviewItemFactory(requireActivity()))
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(AppItemFactory(requireActivity()), ListSeparatorItemFactory(requireActivity()))
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateItemFactory(requireActivity()))
        binding.recyclerRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.recyclerRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.recyclerRefreshLayout.isRefreshing = it == true
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.setDataList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
    }
}