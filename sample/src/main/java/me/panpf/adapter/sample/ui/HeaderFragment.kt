package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fm_header.*
import me.panpf.adapter.sample.R
import me.panpf.args.ktx.bindStringArg

class HeaderFragment : Fragment() {

    private val text: String by bindStringArg("text")
    private val imageUrl by bindStringArg("imageUrl")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerFm_titleText.text = text
        headerFm_image.displayImage(imageUrl)
    }

    companion object {

        fun buildParams(text: String, imageUrl: String) = Bundle().apply {
            putString("text", text)
            putString("imageUrl", imageUrl)
        }
    }
}
