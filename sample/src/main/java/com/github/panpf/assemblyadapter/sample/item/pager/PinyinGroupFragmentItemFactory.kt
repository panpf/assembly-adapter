package com.github.panpf.assemblyadapter.sample.item.pager

import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.pager.fragment.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup

class PinyinGroupFragmentItemFactory : AssemblyFragmentItemFactory<PinyinGroup>() {

    override fun match(data: Any?): Boolean {
        return data is PinyinGroup
    }

    override fun createFragment(position: Int, data: PinyinGroup?): Fragment {
        return PinyinGroupFragment.createInstance(data!!)
    }
}