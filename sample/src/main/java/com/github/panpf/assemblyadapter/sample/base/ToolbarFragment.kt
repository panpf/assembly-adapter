package com.github.panpf.assemblyadapter.sample.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.github.panpf.assemblyadapter.sample.R

abstract class ToolbarFragment<VIEW_BINDING : ViewBinding> : Fragment() {

    protected var binding: VIEW_BINDING? = null
    protected var toolbar: Toolbar? = null

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_toolbar, container, false).apply {
            val toolbar = findViewById<Toolbar>(R.id.toolbarFragmentToolbar)
            val contentContainer = findViewById<FrameLayout>(R.id.toolbarFragmentContent)

            val binding = createViewBinding(inflater, contentContainer)
            contentContainer.addView(binding.root)

            this@ToolbarFragment.toolbar = toolbar
            this@ToolbarFragment.binding = binding
        }
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = this.binding!!
        val toolbar = this.toolbar!!
        onInitViews(toolbar, binding, savedInstanceState)
        onInitData(toolbar, binding, savedInstanceState)
    }

    override fun onDestroyView() {
        this.toolbar = null
        this.binding = null
        super.onDestroyView()
    }

    protected abstract fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): VIEW_BINDING

    protected open fun onInitViews(
        toolbar: Toolbar,
        binding: VIEW_BINDING,
        savedInstanceState: Bundle?
    ) {

    }

    protected abstract fun onInitData(
        toolbar: Toolbar,
        binding: VIEW_BINDING,
        savedInstanceState: Bundle?
    )
}