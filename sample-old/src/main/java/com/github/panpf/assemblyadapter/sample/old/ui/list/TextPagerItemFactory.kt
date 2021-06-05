package com.github.panpf.assemblyadapter.sample.old.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import me.panpf.adapter.pager.AssemblyPagerItemFactory
import com.github.panpf.assemblyadapter.sample.old.R
import com.github.panpf.assemblyadapter.sample.old.bean.Text

class TextPagerItemFactory : AssemblyPagerItemFactory<Text>() {
    override fun match(data: Any?): Boolean {
        return data is Text
    }

    override fun createView(context: Context, container: ViewGroup, position: Int, text: Text?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fm_text, container, false)

        val textView = view.findViewById(R.id.textFm_text) as TextView
        textView.text = text?.text

        return textView
    }
}
