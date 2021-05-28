package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import me.panpf.adapter.sample.databinding.FmHeaderBinding
import me.panpf.args.ktx.bindStringArg

class HeaderFragment : BaseBindingFragment<FmHeaderBinding>() {

    private val text: String by bindStringArg("text")
    private val imageUrl by bindStringArg("imageUrl")

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmHeaderBinding {
        return FmHeaderBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmHeaderBinding, savedInstanceState: Bundle?) {
        binding.headerFmTitleText.text = text
        binding.headerFmImage.displayImage(imageUrl)
    }

    companion object {

        fun buildParams(text: String, imageUrl: String) = Bundle().apply {
            putString("text", text)
            putString("imageUrl", imageUrl)
        }
    }
}
