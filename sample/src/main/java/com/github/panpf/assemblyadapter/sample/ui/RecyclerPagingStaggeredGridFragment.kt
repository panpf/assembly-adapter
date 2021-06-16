package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.fondesa.recyclerviewdivider.staggeredDividerBuilder
import com.github.panpf.assemblyadapter.recycler.AssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.ItemSpan
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter.recycler.paging.KeyDiffItemCallback
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGridCardItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.MyLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.InstalledAppPinyinFlatPagingViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecyclerPagingStaggeredGridFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<InstalledAppPinyinFlatPagingViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val pagingDataAdapter = AssemblyPagingDataAdapter(
            listOf(AppGridCardItemFactory(), PinyinGroupItemFactory(true)),
            KeyDiffItemCallback()
        ).apply {
            setGridLayoutItemSpan(PinyinGroupItemFactory::class.java, ItemSpan.fullSpan())
        }
        binding.recyclerRecycler.apply {
            adapter = pagingDataAdapter.withLoadStateFooter(MyLoadStateAdapter())
            layoutManager = AssemblyStaggeredGridLayoutManager(3)
            addItemDecoration(context.staggeredDividerBuilder().asSpace().size(20.dp2px).build())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pinyinFlatAppListDataFlow.collect {
                pagingDataAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataAdapter.loadStateFlow.collect {
                binding.recyclerProgressBar.isVisible = it.refresh is LoadState.Loading
            }
        }
    }
}