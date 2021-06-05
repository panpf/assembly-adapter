package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.databinding.FmImageBinding

class ImageFragment : BaseBindingFragment<FmImageBinding>() {

    companion object {
        fun buildParams(imageUrl: String): Bundle =
            Bundle().apply { putString("imageUrl", imageUrl) }
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmImageBinding {
        return FmImageBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmImageBinding, savedInstanceState: Bundle?) {
        binding.imageFmImage.displayImage(arguments?.getString("imageUrl"))
    }
}
