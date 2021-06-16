package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppGroupBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGridCardItemFactory
import com.github.panpf.tools4a.dimen.ktx.dp2px

class AppGroupFragment : BaseBindingFragment<FragmentAppGroupBinding>() {

    companion object {
        fun createInstance(appGroup: AppGroup) = AppGroupFragment().apply {
            arguments = bundleOf("appGroup" to appGroup)
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentAppGroupBinding {
        return FragmentAppGroupBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentAppGroupBinding, savedInstanceState: Bundle?) {
        val data = arguments?.getParcelable<AppGroup>("appGroup")
        binding.appGroupGroupNameText.text = data?.title
        binding.appGroupAppCountText.text = data?.appList?.size?.toString()
        binding.appGroupRecycler.apply {
            adapter = AssemblyRecyclerAdapter<Any>(listOf(AppGridCardItemFactory()), data?.appList)
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(
                context.dividerBuilder().asSpace()
                    .showSideDividers().showLastDivider()
                    .size(20.dp2px).build()
            )
        }
    }
}