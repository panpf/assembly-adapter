package com.github.panpf.assemblyadapter.sample.item.pager

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppGroupBinding
import com.github.panpf.assemblyadapter.sample.item.AppCardGridItemFactory
import com.github.panpf.tools4a.dimen.ktx.dp2px

class AppGroupFragment : BaseBindingFragment<FragmentAppGroupBinding>() {

    companion object {
        fun createInstance(appGroup: AppGroup) = AppGroupFragment().apply {
            arguments = bundleOf("appGroup" to appGroup)
        }
    }

    private val appGroup by lazy { arguments?.getParcelable<AppGroup>("appGroup") }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentAppGroupBinding {
        return FragmentAppGroupBinding.inflate(inflater, parent, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appGroup
        println("AppGroupFragment. onCreate. $appGroup")
    }

    override fun onInitData(binding: FragmentAppGroupBinding, savedInstanceState: Bundle?) {
        val data = appGroup
        binding.appGroupGroupNameText.text = data?.title
        binding.appGroupAppCountText.text = data?.appList?.size?.toString()
        binding.appGroupRecycler.apply {
            adapter = AssemblyRecyclerAdapter<Any>(listOf(AppCardGridItemFactory()), data?.appList)
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(
                context.dividerBuilder().asSpace()
                    .showSideDividers().showLastDivider()
                    .size(20.dp2px).build()
            )
        }
        println("AppGroupFragment. onInitData. $appGroup")
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        println("AppGroupFragment. onAttachFragment. $appGroup")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("AppGroupFragment. onAttach. $appGroup")
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        println("AppGroupFragment. onAttach. $appGroup")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("AppGroupFragment. onDestroyView. $appGroup")
    }

    override fun onInitViews(binding: FragmentAppGroupBinding, savedInstanceState: Bundle?) {
        super.onInitViews(binding, savedInstanceState)
        println("AppGroupFragment. onInitViews. $appGroup")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        println("AppGroupFragment. onConfigurationChanged. $appGroup")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        println("AppGroupFragment. onLowMemory. $appGroup")
    }

    override fun onStart() {
        super.onStart()
        println("AppGroupFragment. onStart. $appGroup")
    }

    override fun onResume() {
        super.onResume()
        println("AppGroupFragment. onResume. $appGroup")
    }

    override fun onPause() {
        super.onPause()
        println("AppGroupFragment. onPause. $appGroup")
    }

    override fun onStop() {
        super.onStop()
        println("AppGroupFragment. onStop. $appGroup")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("AppGroupFragment. onDestroy. $appGroup")
    }

    override fun onDetach() {
        super.onDetach()
        println("AppGroupFragment. onDetach. $appGroup")
    }
}