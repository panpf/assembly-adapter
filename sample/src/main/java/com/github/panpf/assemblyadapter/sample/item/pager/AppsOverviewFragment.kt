package com.github.panpf.assemblyadapter.sample.item.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppsOverviewBinding

class AppsOverviewFragment : BaseBindingFragment<FragmentAppsOverviewBinding>() {

    companion object {
        fun createInstance(appsOverview: AppsOverview) = AppsOverviewFragment().apply {
            arguments = bundleOf("appsOverview" to appsOverview)
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentAppsOverviewBinding {
        return FragmentAppsOverviewBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentAppsOverviewBinding, savedInstanceState: Bundle?) {
        val data = arguments?.getParcelable<AppsOverview>("appsOverview")
        binding.appsOverviewContentText.text = requireContext().getString(
            R.string.apps_overview,
            data?.count ?: 0,
            data?.userAppCount ?: 0,
            data?.groupCount ?: 0
        )
    }
}