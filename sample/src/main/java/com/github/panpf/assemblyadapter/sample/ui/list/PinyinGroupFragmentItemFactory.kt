package com.github.panpf.assemblyadapter.sample.ui.list

import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.panpf.assemblyadapter.sample.ui.PinyinGroupFragment

class PinyinGroupFragmentItemFactory : AssemblyFragmentItemFactory<PinyinGroup>() {

    override fun match(data: Any?): Boolean {
        return data is PinyinGroup
    }

    override fun createFragment(position: Int, data: PinyinGroup?): Fragment {
        return PinyinGroupFragment.createInstance(data!!)
    }
}