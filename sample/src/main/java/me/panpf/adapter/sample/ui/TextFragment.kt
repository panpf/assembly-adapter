package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fm_text.*
import me.panpf.adapter.sample.R
import me.panpf.args.ktx.bindStringArg

class TextFragment : Fragment() {

    private val text by bindStringArg("text")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textFm_text.text = text
    }

    companion object {
        fun buildParams(text: String): Bundle = Bundle().apply { putString("text", text) }
    }
}
