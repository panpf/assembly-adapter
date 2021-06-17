package com.github.panpf.assemblyadapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.LoadStateParcelable
import com.github.panpf.assemblyadapter.sample.databinding.FragmentLoadStateBinding

class LoadStateFragment : BaseBindingFragment<FragmentLoadStateBinding>() {

    companion object {
        fun createInstance(loadState: LoadState) = LoadStateFragment().apply {
            arguments = bundleOf("loadStateParcelable" to LoadStateParcelable(loadState))
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentLoadStateBinding {
        return FragmentLoadStateBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentLoadStateBinding, savedInstanceState: Bundle?) {
        val data =
            arguments?.getParcelable<LoadStateParcelable>("loadStateParcelable")?.toLoadState()
        binding.loadStateLoadingLayout.isVisible = data is LoadState.Loading
        binding.loadStateErrorText.isVisible = data is LoadState.Error
    }
}