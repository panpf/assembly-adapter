package com.github.panpf.assemblyadapter.sample.old.ui.list

import androidx.fragment.app.Fragment

import me.panpf.adapter.pager.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.old.bean.Text
import com.github.panpf.assemblyadapter.sample.old.ui.TextFragment

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
