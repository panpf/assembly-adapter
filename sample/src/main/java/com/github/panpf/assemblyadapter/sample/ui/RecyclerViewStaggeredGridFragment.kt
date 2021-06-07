package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.fondesa.recyclerviewdivider.staggeredDividerBuilder
import com.github.panpf.assemblyadapter.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.AssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.ItemSpan
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGridCardItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.InstalledAppListPinyinFlatViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerViewStaggeredGridFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

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
            setItemSpanInGridLayoutManager(PinyinGroupItemFactory::class.java, ItemSpan.fullSpan())
        }
        binding.recyclerRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = AssemblyStaggeredGridLayoutManager(3)
            addItemDecoration(context.staggeredDividerBuilder().asSpace().size(20.dp2px).build())
        }

        installedAppListPinyinFlatViewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.setDataList(it)
        }

        installedAppListPinyinFlatViewModel.loadingData.observe(viewLifecycleOwner) {
            binding.recyclerProgressBar.isVisible = it == true
        }
    }
}