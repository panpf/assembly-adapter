package me.xiaopan.assemblyadaptersample.itemfactory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import me.xiaopan.assemblyadapter.AssemblyPagerItemFactory
import me.xiaopan.assemblyadaptersample.R
import me.xiaopan.assemblyadaptersample.bean.Header
import me.xiaopan.sketch.SketchImageView

class HeaderPagerItemFactory(private val clickListener: View.OnClickListener) : AssemblyPagerItemFactory<Header>() {

    override fun isTarget(data: Any): Boolean {
        return data is Header
    }

    override fun createView(context: Context, container: ViewGroup, position: Int, header: Header): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_header_image, container, false)

        val textView = view.findViewById(R.id.text_headerImageFragment) as TextView
        textView.text = header.text

        val imageView = view.findViewById(R.id.image_headerImageFragment) as SketchImageView
        imageView.displayImage(header.imageUrl)

        view.setOnClickListener(clickListener)

        return view
    }
}
