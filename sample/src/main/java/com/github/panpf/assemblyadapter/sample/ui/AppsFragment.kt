package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.Apps
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppsBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppCardGridItemFactory
import com.github.panpf.tools4a.dimen.ktx.dp2px

class AppsFragment : BaseBindingFragment<FragmentAppsBinding>() {

    companion object {
        fun createInstance(apps: Apps) = AppsFragment().apply {
            arguments = bundleOf("apps" to apps)
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentAppsBinding {
        return FragmentAppsBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentAppsBinding, savedInstanceState: Bundle?) {
        val data = arguments?.getParcelable<Apps>("apps")
        binding.appsRecycler.apply {
            adapter = AssemblyRecyclerAdapter<Any>(listOf(AppCardGridItemFactory()), data?.appList)
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(
                context.dividerBuilder().asSpace()
                    .showSideDividers().showFirstDivider().showLastDivider()
                    .size(20.dp2px).build()
            )
        }
    }
}