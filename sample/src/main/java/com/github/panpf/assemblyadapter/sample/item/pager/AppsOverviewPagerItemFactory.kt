package com.github.panpf.assemblyadapter.sample.item.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.pager.BindingPagerItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppsOverviewBinding

class AppsOverviewPagerItemFactory :
    BindingPagerItemFactory<AppsOverview, FragmentAppsOverviewBinding>() {

    override fun match(data: Any): Boolean {
        return data is AppsOverview
    }

    override fun createItemViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        position: Int,
        data: AppsOverview
    ): FragmentAppsOverviewBinding =
        FragmentAppsOverviewBinding.inflate(inflater, parent, false).apply {
            appsOverviewContentText.text = context.getString(
                R.string.apps_overview, data.count, data.userAppCount, data.groupCount
            )
        }
}