package me.xiaopan.assemblyadaptersample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import me.xiaopan.assemblyadaptersample.R
import me.xiaopan.assemblyadaptersample.bindView

class TextFragment : Fragment() {

    var text: String? = null

    val textView: TextView by bindView(R.id.text_imageFragment_content)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val params = arguments
        if (params != null) {
            text = params.getString("text")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_text, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView.text = text
    }

    companion object {

        fun buildParams(text: String): Bundle {
            val bundle = Bundle()
            bundle.putString("text", text)
            return bundle
        }
    }
}
