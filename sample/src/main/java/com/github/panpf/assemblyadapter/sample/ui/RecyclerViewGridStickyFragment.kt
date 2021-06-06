package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.github.panpf.assemblyadapter.recycler.AssemblyGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.ItemSpan
import com.github.panpf.assemblyadapter.sample.base.StickyAssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGridItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupStickyItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinGroupInstalledAppListViewModel
import me.panpf.recycler.sticky.StickyRecyclerItemDecoration

class RecyclerViewGridStickyFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val installedAppListViewModel by viewModels<PinyinGroupInstalledAppListViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val recyclerAdapter = StickyAssemblyRecyclerAdapter<Any>(
            listOf(AppGridItemFactory(), PinyinGroupStickyItemFactory())
        ).apply {
            setItemSpanInGridLayoutManager(
                PinyinGroupStickyItemFactory::class.java, ItemSpan.fullSpan()
            )
        }
        binding.recyclerRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = AssemblyGridLayoutManager(requireContext(), 3)
            addItemDecoration(StickyRecyclerItemDecoration(binding.recyclerStickyContainer))
        }

        installedAppListViewModel.pinyinGroupInstalledAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.setDataList(it)
        }

        installedAppListViewModel.loadingData.observe(viewLifecycleOwner) {
            binding.recyclerProgressBar.isVisible = it == true
        }
    }
}