package me.panpf.adapter.sample.itemfactory

import android.support.v4.app.Fragment

import me.panpf.adapter.pager.AssemblyFragmentItemFactory
import me.panpf.adapter.sample.bean.Header
import me.panpf.adapter.sample.fragment.HeaderFragment

class HeaderFragmentItemFactory : AssemblyFragmentItemFactory<Header>() {
    override fun isTarget(data: Any): Boolean {
        return data is Header
    }

    override fun createFragment(position: Int, header: Header): Fragment {
        val headerFragment = HeaderFragment()
        headerFragment.arguments = HeaderFragment.buildParams(header.text, header.imageUrl)
        return headerFragment
    }
}
