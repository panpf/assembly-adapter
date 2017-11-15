package me.panpf.assemblyadapter.sample.itemfactory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import me.panpf.assemblyadapter.AssemblyPagerItemFactory
import me.panpf.assemblyadapter.sample.R
import me.panpf.assemblyadapter.sample.bean.Text

class TextPagerItemFactory : AssemblyPagerItemFactory<Text>() {
    override fun isTarget(data: Any): Boolean {
        return data is Text
    }

    override fun createView(context: Context, container: ViewGroup, position: Int, text: Text): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_text, container, false)

        val textView = view.findViewById(R.id.text_imageFragment_content) as TextView
        textView.text = text.text

        return textView
    }
}
