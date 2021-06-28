package com.github.panpf.assemblyadapter.pager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

class ViewFragmentItemFactory<DATA>(
    private val dataClazz: Class<DATA>,
    @LayoutRes private val layoutResId: Int
) : FragmentItemFactory<DATA>() {

    override fun match(data: Any): Boolean {
        return dataClazz.isInstance(data)
    }

    override fun createFragment(position: Int, data: DATA): Fragment {
        return ViewFragment.createInstance(layoutResId)
    }

    class ViewFragment : Fragment() {
        companion object {
            fun createInstance(@LayoutRes layoutResId: Int) = ViewFragment().apply {
                arguments = Bundle().apply { putInt("layoutResId", layoutResId) }
            }
        }

        private val layoutResId by lazy { arguments?.getInt("layoutResId")!! }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View = inflater.inflate(layoutResId, container, false)
    }
}