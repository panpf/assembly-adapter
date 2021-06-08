package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.AssemblyGridLayoutManager
import com.github.panpf.assemblyadapter.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.ItemSpan
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGridCardItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.InstalledAppListPinyinFlatViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerViewGridFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val installedAppListPinyinFlatViewModel by viewModels<InstalledAppListPinyinFlatViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(AppGridCardItemFactory(), PinyinGroupItemFactory(true))
        ).apply {
            setGridLayoutItemSpan(PinyinGroupItemFactory::class.java, ItemSpan.fullSpan())
        }
        binding.recyclerRecycler.apply {
            updatePadding(left = 20.dp2px, right = 20.dp2px)
            clipChildren = false
            adapter = recyclerAdapter
            layoutManager = AssemblyGridLayoutManager(requireContext(), 3)
            addItemDecoration(context.dividerBuilder().asSpace().size(20.dp2px).build())
        }

        installedAppListPinyinFlatViewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.setDataList(it)
        }

        installedAppListPinyinFlatViewModel.loadingData.observe(viewLifecycleOwner) {
            binding.recyclerProgressBar.isVisible = it == true
        }
    }
}