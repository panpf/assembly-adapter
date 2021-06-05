package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.databinding.FmHeaderBinding

class HeaderFragment : BaseBindingFragment<FmHeaderBinding>() {

    companion object {
        fun buildParams(text: String, imageUrl: String) = Bundle().apply {
            putString("text", text)
            putString("imageUrl", imageUrl)
        }
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmHeaderBinding {
        return FmHeaderBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmHeaderBinding, savedInstanceState: Bundle?) {
        binding.headerFmTitleText.text = arguments?.getString("text")
        binding.headerFmImage.displayImage(arguments?.getString("imageUrl"))
    }
}
