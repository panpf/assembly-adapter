package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPinyinGroupBinding

class PinyinGroupFragment : BaseBindingFragment<FragmentPinyinGroupBinding>() {

    companion object {
        fun createInstance(pinyinGroup: PinyinGroup) = PinyinGroupFragment().apply {
            arguments = bundleOf("pinyinGroup" to pinyinGroup)
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPinyinGroupBinding {
        return FragmentPinyinGroupBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPinyinGroupBinding, savedInstanceState: Bundle?) {
        val data = arguments?.getParcelable<PinyinGroup>("pinyinGroup")
        binding.pinyinGroupGroupNameText.text = data?.title
        binding.pinyinGroupAppCountText.text = data?.childSize?.toString()
    }
}