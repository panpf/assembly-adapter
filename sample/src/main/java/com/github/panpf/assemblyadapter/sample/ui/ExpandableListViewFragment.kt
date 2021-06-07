package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.github.panpf.assemblyadapter.list.AssemblyExpandableListAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.databinding.FragmentExpandableListBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGroupItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.AppItemFactory
import com.github.panpf.assemblyadapter.sample.vm.InstalledAppListPinyinGroupViewModel

class ExpandableListViewFragment : BaseBindingFragment<FragmentExpandableListBinding>() {

    private val installedAppListPinyinGroupViewModel by viewModels<InstalledAppListPinyinGroupViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentExpandableListBinding {
        return FragmentExpandableListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentExpandableListBinding, savedInstanceState: Bundle?) {
        val listAdapter =
            AssemblyExpandableListAdapter<AppGroup, AppInfo>(
                listOf(AppGroupItemFactory(), AppItemFactory())
            )
        binding.expandableListList.setAdapter(listAdapter)

        installedAppListPinyinGroupViewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
            listAdapter.setDataList(it)
        }

        installedAppListPinyinGroupViewModel.loadingData.observe(viewLifecycleOwner) {
            binding.expandableListProgressBar.isVisible = it == true
        }
    }
}