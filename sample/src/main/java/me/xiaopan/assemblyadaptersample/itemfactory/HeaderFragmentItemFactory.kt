package me.xiaopan.assemblyadaptersample.itemfactory

import android.support.v4.app.Fragment

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory
import me.xiaopan.assemblyadaptersample.bean.Header
import me.xiaopan.assemblyadaptersample.fragment.HeaderFragment

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
