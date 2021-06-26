package com.github.panpf.assemblyadapter.sample.item.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.pager.PagerItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppsOverviewBinding

class AppsOverviewPagerItemFactory : PagerItemFactory<AppsOverview>() {

    override fun match(data: Any): Boolean {
        return data is AppsOverview
    }

    override fun createView(
        context: Context, container: ViewGroup, position: Int, data: AppsOverview
    ): View =
        FragmentAppsOverviewBinding.inflate(LayoutInflater.from(context), container, false).apply {
            appsOverviewContentText.text = context.getString(
                R.string.apps_overview, data.count, data.userAppCount, data.groupCount
            )
        }.root
}