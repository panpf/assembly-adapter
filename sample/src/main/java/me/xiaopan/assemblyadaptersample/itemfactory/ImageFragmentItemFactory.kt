package me.xiaopan.assemblyadaptersample.itemfactory

import android.support.v4.app.Fragment

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory
import me.xiaopan.assemblyadaptersample.fragment.ImageFragment

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
