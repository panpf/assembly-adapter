package com.github.panpf.assemblyadapter.sample.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import com.fondesa.recyclerviewdivider.staggeredDividerBuilder
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.item.AppCardGridItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerGridStaggeredFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataRecyclerAdapter<AppsOverview>(
            AppsOverviewItemFactory(true)
        )
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(AppCardGridItemFactory(), PinyinGroupItemFactory(true))
        )
        binding.recyclerRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter)
            layoutManager =
                AssemblyStaggeredGridLayoutManager(
                    3,
                    listOf(AppsOverviewItemFactory::class, PinyinGroupItemFactory::class)
                )
            addItemDecoration(context.staggeredDividerBuilder().asSpace().size(20.dp2px).build())
        }
        binding.recyclerRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.setDataList(it)
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.recyclerRefreshLayout.isRefreshing = it == true
        }
    }
}