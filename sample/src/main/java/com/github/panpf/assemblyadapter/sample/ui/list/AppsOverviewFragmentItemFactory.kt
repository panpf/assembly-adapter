package com.github.panpf.assemblyadapter.sample.ui.list

import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.ui.AppsOverviewFragment

class AppsOverviewFragmentItemFactory : AssemblyFragmentItemFactory<AppsOverview>() {

    override fun match(data: Any?): Boolean {
        return data is AppsOverview
    }

    override fun createFragment(position: Int, data: AppsOverview?): Fragment {
        return AppsOverviewFragment.createInstance(data!!)
    }
}