package com.github.panpf.assemblyadapter.sample.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.pager.AssemblyPagerItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppGroupBinding
import com.github.panpf.tools4a.dimen.ktx.dp2px

class AppGroupPagerItemFactory : AssemblyPagerItemFactory<AppGroup>() {

    override fun match(data: Any?): Boolean {
        return data is AppGroup
    }

    override fun createView(
        context: Context, container: ViewGroup, position: Int, data: AppGroup?
    ): View =
        FragmentAppGroupBinding.inflate(LayoutInflater.from(context), container, false).apply {
            appGroupGroupNameText.text = data?.title
            appGroupAppCountText.text = data?.appList?.size?.toString()
            appGroupRecycler.apply {
                adapter = AssemblyRecyclerAdapter<Any>(
                    listOf(AppGridCardItemFactory()),
                    data?.appList
                )
                layoutManager = GridLayoutManager(context, 3)
                addItemDecoration(
                    context.dividerBuilder().asSpace()
                        .showSideDividers().showLastDivider()
                        .size(20.dp2px).build()
                )
            }
        }.root
}