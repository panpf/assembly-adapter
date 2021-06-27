package com.github.panpf.assemblyadapter.sample.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.item.*
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel

class RecyclerPlaceholderFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()
    private var registered = false

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter =
            AssemblySingleDataRecyclerAdapter(AppsOverviewItemFactory(requireActivity()))
        val recyclerAdapter = AssemblyRecyclerAdapter(
            listOf(AppItemFactory(requireActivity()), ListSeparatorItemFactory(requireActivity())),
            AppPlaceholderItemFactory(),
            arrayOfNulls<Any?>(100).toList()
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateItemFactory(requireActivity()))
        binding.recyclerRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager = LinearLayoutManager(requireContext())
        }

        registered = false
        binding.recyclerRefreshLayout.setOnRefreshListener {
            if (!registered) {
                registered = true

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
            viewModel.refresh()
        }

        Toast.makeText(
            requireContext(),
            "Pull down to refresh to load real data",
            Toast.LENGTH_LONG
        ).show()
    }
}