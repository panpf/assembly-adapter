package com.github.panpf.assemblyadapter.sample.old.ui.list

import androidx.fragment.app.Fragment
import me.panpf.adapter.pager.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.old.bean.Header
import com.github.panpf.assemblyadapter.sample.old.ui.HeaderFragment

class HeaderFragmentItemFactory : AssemblyFragmentItemFactory<Header>() {
    override fun match(data: Any?): Boolean {
        return data is Header
    }

    override fun createFragment(position: Int, header: Header?): Fragment {
        val headerFragment = HeaderFragment()
        if (header != null) {
            headerFragment.arguments = HeaderFragment.buildParams(header.text, header.imageUrl)
        }
        return headerFragment
    }
}
