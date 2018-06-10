package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fm_image.*
import me.panpf.adapter.sample.R
import me.panpf.args.ktx.bindStringArg

class ImageFragment : Fragment() {

    private val imageUrl by bindStringArg("imageUrl")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageFm_image.displayImage(imageUrl)
    }

    companion object {
        fun buildParams(imageUrl: String): Bundle? = Bundle().apply { putString("imageUrl", imageUrl) }
    }
}
