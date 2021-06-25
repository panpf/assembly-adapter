package com.github.panpf.assemblyadapter.sample.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import com.fondesa.recyclerviewdivider.staggeredDividerBuilder
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter.recycler.paging.KeyDiffItemCallback
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.base.MyLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.item.AppCardGridItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.item.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatPagingAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecyclerPagingGridStaggeredFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<PinyinFlatPagingAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter =
            AssemblySingleDataRecyclerAdapter(AppsOverviewItemFactory(requireActivity(), true))
        val pagingDataAdapter = AssemblyPagingDataAdapter(
            listOf(
                AppCardGridItemFactory(requireActivity()),
                PinyinGroupItemFactory(requireActivity(), true)
            ),
            KeyDiffItemCallback()
        )
        binding.recyclerRecycler.apply {
            adapter = ConcatAdapter(
                appsOverviewAdapter,
                pagingDataAdapter.withLoadStateFooter(MyLoadStateAdapter(requireActivity()))
            )
            layoutManager = AssemblyStaggeredGridLayoutManager(
                3,
                listOf(
                    AppsOverviewItemFactory::class,
                    PinyinGroupItemFactory::class,
                    LoadStateItemFactory::class
                )
            )
            addItemDecoration(context.staggeredDividerBuilder().asSpace().size(20.dp2px).build())
        }
        binding.recyclerRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            pagingDataAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataAdapter.loadStateFlow.collect {
                binding.recyclerRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pinyinFlatAppListDataFlow.collect {
                pagingDataAdapter.submitData(it)
            }
        }
    }
}