package com.github.panpf.assemblyadapter.sample.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppsOverviewBinding

class AppsOverviewItemFactory(private val hideStartMargin: Boolean = false) :
    BindingAssemblyItemFactory<AppsOverview, ItemAppsOverviewBinding>() {

    override fun match(data: Any?): Boolean {
        return data is AppsOverview
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppsOverviewBinding {
        return ItemAppsOverviewBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemAppsOverviewBinding,
        item: BindingAssemblyItem<AppsOverview, ItemAppsOverviewBinding>
    ) {
        super.initItem(context, binding, item)
        if (hideStartMargin) {
            binding.root.updatePadding(left = 0, right = 0)
        }
    }

    override fun bindData(
        context: Context, binding: ItemAppsOverviewBinding,
        item: BindingAssemblyItem<AppsOverview, ItemAppsOverviewBinding>,
        position: Int, data: AppsOverview?
    ) {
        binding.appsOverviewItemContentText.text = context.getString(
            R.string.apps_overview_item,
            data?.count ?: 0,
            data?.userAppCount ?: 0,
            data?.groupCount ?: 0
        )
    }
}
