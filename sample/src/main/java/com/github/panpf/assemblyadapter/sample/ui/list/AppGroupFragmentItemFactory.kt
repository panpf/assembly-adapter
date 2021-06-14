package com.github.panpf.assemblyadapter.sample.ui.list

import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.ui.AppGroupFragment

class AppGroupFragmentItemFactory : AssemblyFragmentItemFactory<AppGroup>() {

    override fun match(data: Any?): Boolean {
        return data is AppGroup
    }

    override fun createFragment(position: Int, data: AppGroup?): Fragment {
        return AppGroupFragment.createInstance(data!!)
    }
}