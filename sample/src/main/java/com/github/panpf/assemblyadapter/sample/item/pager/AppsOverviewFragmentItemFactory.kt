package com.github.panpf.assemblyadapter.sample.item.pager

import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.pager.fragment.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview

class AppsOverviewFragmentItemFactory : AssemblyFragmentItemFactory<AppsOverview>() {

    override fun match(data: Any?): Boolean {
        return data is AppsOverview
    }

    override fun createFragment(position: Int, data: AppsOverview?): Fragment {
        return AppsOverviewFragment.createInstance(data!!)
    }
}