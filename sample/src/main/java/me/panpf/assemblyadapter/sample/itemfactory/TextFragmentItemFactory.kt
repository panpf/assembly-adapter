package me.panpf.assemblyadapter.sample.itemfactory

import android.support.v4.app.Fragment

import me.panpf.assemblyadapter.AssemblyFragmentItemFactory
import me.panpf.assemblyadapter.sample.bean.Text
import me.panpf.assemblyadapter.sample.fragment.TextFragment

class TextFragmentItemFactory : AssemblyFragmentItemFactory<Text>() {
    override fun isTarget(data: Any): Boolean {
        return data is Text
    }

    override fun createFragment(position: Int, text: Text): Fragment {
        val textFragment = TextFragment()
        textFragment.arguments = TextFragment.buildParams(text.text)
        return textFragment
    }
}
