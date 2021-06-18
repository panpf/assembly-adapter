package com.github.panpf.assemblyadapter.sample.item.pager

import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.bean.Apps

class AppsFragmentItemFactory : AssemblyFragmentItemFactory<Apps>() {

    override fun match(data: Any?): Boolean {
        return data is Apps
    }

    override fun createFragment(position: Int, data: Apps?): Fragment {
        return AppsFragment.createInstance(data!!)
    }
}