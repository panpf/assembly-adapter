package me.xiaopan.assemblyadaptersample.itemfactory

import android.support.v4.app.Fragment

import me.xiaopan.assemblyadapter.AssemblyFragmentItemFactory
import me.xiaopan.assemblyadaptersample.bean.Text
import me.xiaopan.assemblyadaptersample.fragment.TextFragment

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
