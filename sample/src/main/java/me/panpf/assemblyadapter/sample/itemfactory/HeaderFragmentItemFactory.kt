package me.panpf.assemblyadapter.sample.itemfactory

import android.support.v4.app.Fragment

import me.panpf.assemblyadapter.AssemblyFragmentItemFactory
import me.panpf.assemblyadapter.sample.bean.Header
import me.panpf.assemblyadapter.sample.fragment.HeaderFragment

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
