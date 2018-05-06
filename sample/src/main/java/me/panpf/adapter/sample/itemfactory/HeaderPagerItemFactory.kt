package me.panpf.adapter.sample.itemfactory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import me.panpf.adapter.pager.AssemblyPagerItemFactory
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Header
import me.panpf.sketch.SketchImageView

class HeaderPagerItemFactory : AssemblyPagerItemFactory<Header>() {

    init {
        setOnViewClickListener(R.id.text_headerImageFragment) { view, position, positionInPart, data ->
            Toast.makeText(view.context, "你戳我干嘛！", Toast.LENGTH_SHORT).show()
        }
    }

    override fun match(data: Any?): Boolean {
        return data is Header
    }

    override fun createView(context: Context, container: ViewGroup, position: Int, header: Header?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_header_image, container, false)

        val textView = view.findViewById(R.id.text_headerImageFragment) as TextView
        textView.text = header?.text

        val imageView = view.findViewById(R.id.image_headerImageFragment) as SketchImageView
        imageView.displayImage(header?.imageUrl ?: "")

        return view
    }
}
