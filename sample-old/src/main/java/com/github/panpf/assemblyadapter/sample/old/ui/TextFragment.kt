package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.databinding.FmTextBinding

class TextFragment : BaseBindingFragment<FmTextBinding>() {

    companion object {
        fun buildParams(text: String): Bundle = Bundle().apply { putString("text", text) }
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmTextBinding {
        return FmTextBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmTextBinding, savedInstanceState: Bundle?) {
        binding.textFmText.text = arguments?.getString("text")
    }
}
