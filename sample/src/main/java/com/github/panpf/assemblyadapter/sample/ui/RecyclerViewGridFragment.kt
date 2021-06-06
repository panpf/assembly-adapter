package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.github.panpf.assemblyadapter.recycler.AssemblyGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.ItemSpan
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGridItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinGroupInstalledAppListViewModel

class RecyclerViewGridFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val installedAppListViewModel by viewModels<PinyinGroupInstalledAppListViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(AppGridItemFactory(), PinyinGroupItemFactory())
        ).apply {
            setItemSpanInGridLayoutManager(PinyinGroupItemFactory::class.java, ItemSpan.fullSpan())
        }
        binding.recyclerRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = AssemblyGridLayoutManager(requireContext(), 3)
        }

        installedAppListViewModel.pinyinGroupInstalledAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.setDataList(it)
        }

        installedAppListViewModel.loadingData.observe(viewLifecycleOwner) {
            binding.recyclerProgressBar.isVisible = it == true
        }
    }
}