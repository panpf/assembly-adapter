package com.github.panpf.assemblyadapter.sample.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BaseBindingFragment<VIEW_BINDING : ViewBinding> : BaseFragment() {

    protected var binding: VIEW_BINDING? = null

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = createViewBinding(inflater, container)
        this.binding = binding
        return binding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = this.binding!!
        onInitViews(binding, savedInstanceState)
        onInitData(binding, savedInstanceState)
    }

    override fun onDestroyView() {
        this.binding = null
        super.onDestroyView()
    }

    protected abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): VIEW_BINDING

    protected open fun onInitViews(binding: VIEW_BINDING, savedInstanceState: Bundle?){

    }

    protected abstract fun onInitData(binding: VIEW_BINDING, savedInstanceState: Bundle?)
}