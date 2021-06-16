package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.recycler.AssemblyGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.ItemSpan
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGridCardItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.InstalledAppPinyinFlatViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerGridFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<InstalledAppPinyinFlatViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(AppGridCardItemFactory(), PinyinGroupItemFactory(true))
        )
        binding.recyclerRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = AssemblyGridLayoutManager(
                requireContext(), 3,
                mapOf(PinyinGroupItemFactory::class to ItemSpan.fullSpan())
            )
            addItemDecoration(
                context.dividerBuilder().asSpace()
                    .showSideDividers().showLastDivider()
                    .size(20.dp2px).build()
            )
        }

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.setDataList(it)
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.recyclerProgressBar.isVisible = it == true
        }
    }
}