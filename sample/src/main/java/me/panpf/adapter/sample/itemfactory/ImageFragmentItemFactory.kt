package me.panpf.adapter.sample.itemfactory

import android.support.v4.app.Fragment

import me.panpf.adapter.pager.AssemblyFragmentItemFactory
import me.panpf.adapter.sample.fragment.ImageFragment

class ImageFragmentItemFactory : AssemblyFragmentItemFactory<String>() {
    override fun isTarget(data: Any?): Boolean {
        return data is String
    }

    override fun createFragment(position: Int, string: String?): Fragment {
        if (string == null) throw IllegalArgumentException("data is null")

        val imageFragment = ImageFragment()
        imageFragment.arguments = ImageFragment.buildParams(string)
        return imageFragment
    }
}
