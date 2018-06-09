package me.panpf.adapter.sample.item

import android.support.v4.app.Fragment

import me.panpf.adapter.pager.AssemblyFragmentItemFactory
import me.panpf.adapter.sample.bean.Text
import me.panpf.adapter.sample.ui.TextFragment

class TextFragmentItemFactory : AssemblyFragmentItemFactory<Text>() {
    override fun match(data: Any?): Boolean {
        return data is Text
    }

    override fun createFragment(position: Int, text: Text?): Fragment {
        val textFragment = TextFragment()
        textFragment.arguments = TextFragment.buildParams(text?.text ?: "")
        return textFragment
    }
}
