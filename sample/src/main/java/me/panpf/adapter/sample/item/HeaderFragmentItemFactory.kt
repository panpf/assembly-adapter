package me.panpf.adapter.sample.item

import androidx.fragment.app.Fragment
import me.panpf.adapter.pager.AssemblyFragmentItemFactory
import me.panpf.adapter.sample.bean.Header
import me.panpf.adapter.sample.ui.HeaderFragment

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
