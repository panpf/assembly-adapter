package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter.recycler.paging.KeyDiffItemCallback
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.MyLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.InstalledAppPinyinFlatPagingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecyclerPagingLinearFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<InstalledAppPinyinFlatPagingViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val pagingDataAdapter = AssemblyPagingDataAdapter(
            listOf(AppItemFactory(), PinyinGroupItemFactory()),
            KeyDiffItemCallback()
        )
        binding.recyclerRecycler.apply {
            adapter = pagingDataAdapter.withLoadStateFooter(MyLoadStateAdapter())
            layoutManager = LinearLayoutManager(requireContext())
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