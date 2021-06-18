package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.github.panpf.assemblyadapter.list.AssemblyListAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentListBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel

class ListFragment : BaseBindingFragment<FragmentListBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentListBinding, savedInstanceState: Bundle?) {
        val listAdapter =
            AssemblyListAdapter<Any>(
                listOf(AppItemFactory(), PinyinGroupItemFactory())
            )
        binding.listList.adapter = listAdapter

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            listAdapter.setDataList(it)
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.listProgressBar.isVisible = it == true
        }
    }
}