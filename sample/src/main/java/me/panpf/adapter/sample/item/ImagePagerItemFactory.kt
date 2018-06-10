package me.panpf.adapter.sample.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.panpf.adapter.pager.AssemblyPagerItemFactory
import me.panpf.adapter.sample.R
import me.panpf.sketch.SketchImageView

class ImagePagerItemFactory : AssemblyPagerItemFactory<String>() {
    override fun match(data: Any?): Boolean {
        return data is String
    }

    override fun createView(context: Context, container: ViewGroup, position: Int, imageUrl: String?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fm_image, container, false)

        val imageView = view.findViewById(R.id.imageFm_image) as SketchImageView
        imageView.displayImage(imageUrl ?: "")

        return view
    }
}
