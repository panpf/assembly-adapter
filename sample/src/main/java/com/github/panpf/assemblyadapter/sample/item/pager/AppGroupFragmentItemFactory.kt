package com.github.panpf.assemblyadapter.sample.item.pager

import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.pager.fragment.FragmentItemFactory
import com.github.panpf.assemblyadapter.sample.bean.AppGroup

class AppGroupFragmentItemFactory : FragmentItemFactory<AppGroup>() {

    override fun match(data: Any): Boolean {
        return data is AppGroup
    }

    override fun createFragment(position: Int, data: AppGroup): Fragment {
        return AppGroupFragment.createInstance(data)
    }
}