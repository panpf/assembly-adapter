package com.github.panpf.assemblyadapter.sample.old.ui.list

import androidx.fragment.app.Fragment

import me.panpf.adapter.pager.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.old.ui.ImageFragment

class ImageFragmentItemFactory : AssemblyFragmentItemFactory<String>() {
    override fun match(data: Any?): Boolean {
        return data is String
    }

    override fun createFragment(position: Int, string: String?): Fragment {
        val imageFragment = ImageFragment()
        imageFragment.arguments = ImageFragment.buildParams(string?:"")
        return imageFragment
    }
}
