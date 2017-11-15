package me.xiaopan.assemblyadaptersample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.panpf.sketch.SketchImageView
import me.xiaopan.assemblyadaptersample.R
import me.xiaopan.assemblyadaptersample.bindView

class HeaderFragment : Fragment() {

    var text: String? = null
    private var imageUrl: String? = null

    private val textView: TextView by bindView(R.id.text_headerImageFragment)
    private val imageView: SketchImageView by bindView(R.id.image_headerImageFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val params = arguments
        if (params != null) {
            text = params.getString("text")
            imageUrl = params.getString("imageUrl")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_header_image, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView.text = text
        imageView.displayImage(imageUrl ?: "")
    }

    companion object {

        fun buildParams(text: String, imageUrl: String): Bundle {
            val bundle = Bundle()
            bundle.putString("text", text)
            bundle.putString("imageUrl", imageUrl)
            return bundle
        }
    }
}
