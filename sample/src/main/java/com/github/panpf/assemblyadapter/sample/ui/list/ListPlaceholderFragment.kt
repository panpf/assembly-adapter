package com.github.panpf.assemblyadapter.sample.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.AssemblyListAdapter
import com.github.panpf.assemblyadapter.list.AssemblySingleDataListAdapter
import com.github.panpf.assemblyadapter.list.concat.ConcatListAdapter
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentListBinding
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppPlaceholderBinding
import com.github.panpf.assemblyadapter.sample.item.*
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel

class ListPlaceholderFragment : BaseBindingFragment<FragmentListBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()
    private var registered = false

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentListBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter =
            AssemblySingleDataListAdapter(AppsOverviewItemFactory(requireActivity()))
        val listAdapter = AssemblyListAdapter(
            listOf(AppItemFactory(requireActivity()), ListSeparatorItemFactory(requireActivity())),
            ViewItemFactory(Placeholder::class.java, R.layout.item_app_placeholder),
            arrayOfNulls<Any?>(100).toList()
        )
        val footerLoadStateAdapter =
            AssemblySingleDataListAdapter(LoadStateItemFactory(requireActivity()))
        binding.listList.adapter =
            ConcatListAdapter(appsOverviewAdapter, listAdapter, footerLoadStateAdapter)

        registered = false
        binding.listRefreshLayout.setOnRefreshListener {
            if (!registered) {
                registered = true

                viewModel.loadingData.observe(viewLifecycleOwner) {
                    binding.listRefreshLayout.isRefreshing = it == true
                }

                viewModel.appsOverviewData.observe(viewLifecycleOwner) {
                    appsOverviewAdapter.data = it
                }

                viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
                    listAdapter.setDataList(it)
                    footerLoadStateAdapter.data = LoadState.NotLoading(true)
                }
            }
            viewModel.refresh()
        }

        Toast.makeText(
            requireContext(),
            "Pull down to refresh to load real data",
            Toast.LENGTH_LONG
        ).show()
    }
}