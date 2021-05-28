package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import me.panpf.adapter.sample.databinding.FmImageBinding
import me.panpf.args.ktx.bindStringArg

class ImageFragment : BaseBindingFragment<FmImageBinding>() {

    private val imageUrl by bindStringArg("imageUrl")

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmImageBinding {
        return FmImageBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmImageBinding, savedInstanceState: Bundle?) {
        binding.imageFmImage.displayImage(imageUrl)
    }

    companion object {
        fun buildParams(imageUrl: String): Bundle? =
            Bundle().apply { putString("imageUrl", imageUrl) }
    }
}
