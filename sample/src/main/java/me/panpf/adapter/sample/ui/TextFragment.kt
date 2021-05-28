package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import me.panpf.adapter.sample.databinding.FmTextBinding
import me.panpf.args.ktx.bindStringArg

class TextFragment : BaseBindingFragment<FmTextBinding>() {

    private val text by bindStringArg("text")

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmTextBinding {
        return FmTextBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmTextBinding, savedInstanceState: Bundle?) {
        binding.textFmText.text = text
    }

    companion object {
        fun buildParams(text: String): Bundle = Bundle().apply { putString("text", text) }
    }
}
