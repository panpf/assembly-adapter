package com.github.panpf.assemblyadapter.sample.item.pager

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.pager.BindingPagerItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppGroupBinding
import com.github.panpf.assemblyadapter.sample.item.AppCardGridItemFactory
import com.github.panpf.tools4a.dimen.ktx.dp2px

class AppGroupPagerItemFactory(private val activity: Activity) :
    BindingPagerItemFactory<AppGroup, FragmentAppGroupBinding>() {

    override fun match(data: Any): Boolean {
        return data is AppGroup
    }

    override fun createItemViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        position: Int,
        data: AppGroup
    ): FragmentAppGroupBinding =
        FragmentAppGroupBinding.inflate(inflater, parent, false).apply {
            appGroupGroupNameText.text = data.title
            appGroupAppCountText.text = data.appList.size.toString()
            appGroupRecycler.apply {
                adapter = AssemblyRecyclerAdapter<Any>(
                    listOf(AppCardGridItemFactory(activity)),
                    data.appList
                )
                layoutManager = GridLayoutManager(context, 3)
                addItemDecoration(
                    context.dividerBuilder().asSpace()
                        .showSideDividers().showLastDivider()
                        .size(20.dp2px).build()
                )
            }
        }
}