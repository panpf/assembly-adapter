package com.github.panpf.assemblyadapter.sample.ui.pager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.viewpager.widget.ViewPager
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.pager.AssemblyPagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataPagerAdapter
import com.github.panpf.assemblyadapter.pager.ViewPagerItemFactory
import com.github.panpf.assemblyadapter.pager.concat.ConcatPagerAdapter
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPagerBinding
import com.github.panpf.assemblyadapter.sample.item.pager.AppGroupPagerItemFactory
import com.github.panpf.assemblyadapter.sample.item.pager.AppsOverviewPagerItemFactory
import com.github.panpf.assemblyadapter.sample.item.pager.LoadStatePagerItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PagerPinyinGroupAppsViewModel

class PagerViewPlaceholderFragment : BaseBindingFragment<FragmentPagerBinding>() {

    private val viewModel by viewModels<PagerPinyinGroupAppsViewModel>()
    private var registered = false

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPagerBinding {
        return FragmentPagerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPagerBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataPagerAdapter(AppsOverviewPagerItemFactory())
        val pagerAdapter = AssemblyPagerAdapter(
            listOf(AppGroupPagerItemFactory(requireActivity())),
            ViewPagerItemFactory(Placeholder::class.java, R.layout.fragment_app_group_placeholder),
            arrayOfNulls<Any?>(20).toList()
        )
        val footerLoadStateAdapter = AssemblySingleDataPagerAdapter(LoadStatePagerItemFactory())
        binding.pagerPager.apply {
            adapter = ConcatPagerAdapter(appsOverviewAdapter, pagerAdapter, footerLoadStateAdapter)
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageNumber(binding)
                }
            })
        }
        updatePageNumber(binding)

        registered = false
        binding.pagerPageNumberText.setOnClickListener {
            if (!registered) {
                registered = true

                viewModel.loadingData.observe(viewLifecycleOwner) {
                    binding.pagerProgressBar.isVisible = it == true
                    binding.pagerPageNumberText.isVisible = it != true
                }

                viewModel.appsOverviewData.observe(viewLifecycleOwner) {
                    appsOverviewAdapter.data = it
                    updatePageNumber(binding)
                }

                viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
                    pagerAdapter.setDataList(it)
                    footerLoadStateAdapter.data = LoadState.NotLoading(true)
                    updatePageNumber(binding)
                }
            }
        }

        Toast.makeText(
            requireContext(),
            "Click page number to load real data",
            Toast.LENGTH_LONG
        ).show()
    }

    @SuppressLint("SetTextI18n")
    private fun updatePageNumber(binding: FragmentPagerBinding) {
        val pager = binding.pagerPager
        binding.pagerPageNumberText.text =
            "${pager.currentItem + 1}/${pager.adapter?.count ?: 0}"
    }
}
