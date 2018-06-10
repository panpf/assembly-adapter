package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fm_recycler_sticky.*
import me.panpf.adapter.recycler.AssemblyGridLayoutManager
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.adapter.AssemblyStickyRecyclerAdapter
import me.panpf.adapter.sample.bean.AppsTitle
import me.panpf.adapter.sample.item.AppItem
import me.panpf.adapter.sample.item.AppListHeaderItem
import me.panpf.adapter.sample.vm.AppsViewModel
import me.panpf.arch.ktx.bindViewModel
import me.panpf.recycler.sticky.StickyRecyclerItemDecoration
import java.util.*

class GridRecyclerViewFragment : Fragment() {

    private val appsViewModel by bindViewModel(AppsViewModel::class)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_recycler_sticky, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = context ?: return
        stickyRecyclerFm_recycler.layoutManager = AssemblyGridLayoutManager(context, 3, stickyRecyclerFm_recycler)

        val adapter = AssemblyStickyRecyclerAdapter().apply {
            addItemFactory(AppItem.Factory())
            addItemFactory(AppListHeaderItem.Factory().fullSpan(stickyRecyclerFm_recycler))
        }

        stickyRecyclerFm_recycler.addItemDecoration(StickyRecyclerItemDecoration(stickyRecyclerFm_frame))
        stickyRecyclerFm_recycler.adapter = adapter

        appsViewModel.apps.observe(this, android.arch.lifecycle.Observer {
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
            stickyRecyclerFm_recycler.scheduleLayoutAnimation()
        })

        appsViewModel.load()
    }
}