package me.panpf.adapter.sample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.panpf.sketch.SketchImageView
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bindView

class ImageFragment : Fragment() {

    private var imageUrl: String? = null
    private val imageView: SketchImageView by bindView(R.id.image_imageFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val params = arguments
        if (params != null) {
            imageUrl = params.getString("imageUrl")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView.displayImage(imageUrl ?: "")
    }

    companion object {

        fun buildParams(imageUrl: String): Bundle {
            val bundle = Bundle()
            bundle.putString("imageUrl", imageUrl)
            return bundle
        }
    }
}
