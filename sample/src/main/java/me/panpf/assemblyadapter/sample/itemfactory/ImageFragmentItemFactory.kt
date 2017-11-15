package me.panpf.assemblyadapter.sample.itemfactory

import android.support.v4.app.Fragment

import me.panpf.assemblyadapter.AssemblyFragmentItemFactory
import me.panpf.assemblyadapter.sample.fragment.ImageFragment

class ImageFragmentItemFactory : AssemblyFragmentItemFactory<String>() {
    override fun isTarget(data: Any): Boolean {
        return data is String
    }

    override fun createFragment(position: Int, string: String): Fragment {
        val imageFragment = ImageFragment()
        imageFragment.arguments = ImageFragment.buildParams(string)
        return imageFragment
    }
}
