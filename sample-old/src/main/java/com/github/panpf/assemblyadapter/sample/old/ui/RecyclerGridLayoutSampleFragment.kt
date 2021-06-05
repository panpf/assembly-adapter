package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.panpf.assemblyadapter.sample.old.base.AssemblyStickyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.bean.AppsTitle
import com.github.panpf.assemblyadapter.sample.old.databinding.FmRecyclerStickyBinding
import com.github.panpf.assemblyadapter.sample.old.ui.list.AppItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.AppListHeaderItem
import com.github.panpf.assemblyadapter.sample.old.vm.AppsViewModel
import me.panpf.adapter.recycler.AssemblyGridLayoutManager
import me.panpf.recycler.sticky.StickyRecyclerItemDecoration
import java.util.*

class RecyclerGridLayoutSampleFragment : BaseBindingFragment<FmRecyclerStickyBinding>() {

    private val appsViewModel by viewModels<AppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FmRecyclerStickyBinding {
        return FmRecyclerStickyBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmRecyclerStickyBinding, savedInstanceState: Bundle?) {
        val context = context ?: return
        binding.stickyRecyclerFmRecycler.layoutManager =
            AssemblyGridLayoutManager(context, 3, binding.stickyRecyclerFmRecycler)

        val adapter = AssemblyStickyRecyclerAdapter().apply {
            addItemFactory(AppItem.Factory())
            addItemFactory(AppListHeaderItem.Factory().fullSpan(binding.stickyRecyclerFmRecycler))
        }

        binding.stickyRecyclerFmRecycler.addItemDecoration(StickyRecyclerItemDecoration(binding.stickyRecyclerFmFrame))
        binding.stickyRecyclerFmRecycler.adapter = adapter

        appsViewModel.apps.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it ?: return@Observer

            val systemAppList = it[0]
            val userAppList = it[1]

            val systemAppListSize = systemAppList.size
            val userAppListSize = userAppList.size

            var dataListSize = if (systemAppListSize > 0) systemAppListSize + 1 else 0
            dataListSize += if (userAppListSize > 0) userAppListSize + 1 else 0

            val dataList = ArrayList<Any>(dataListSize)
            if (userAppListSize > 0) {
                dataList.add(AppsTitle(String.format("自安装应用 %d 个", userAppListSize)))
                dataList.addAll(userAppList)
            }
            if (systemAppListSize > 0) {
                dataList.add(AppsTitle(String.format("系统应用 %d 个", systemAppListSize)))
                dataList.addAll(systemAppList)
            }

            adapter.dataList = dataList
            binding.stickyRecyclerFmRecycler.scheduleLayoutAnimation()
        })

        appsViewModel.load()
    }
}