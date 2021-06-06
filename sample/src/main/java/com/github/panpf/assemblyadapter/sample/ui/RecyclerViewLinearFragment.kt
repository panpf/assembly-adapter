package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinGroupInstalledAppListViewModel

class RecyclerViewLinearFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val installedAppListViewModel by viewModels<PinyinGroupInstalledAppListViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(AppItemFactory(), PinyinGroupItemFactory())
        )
        binding.recyclerRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        installedAppListViewModel.pinyinGroupInstalledAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.setDataList(it)
        }

        installedAppListViewModel.loadingData.observe(viewLifecycleOwner) {
            binding.recyclerProgressBar.isVisible = it == true
        }
    }
}