package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.base.End
import com.github.panpf.assemblyadapter.sample.old.databinding.FmRecyclerBinding
import com.github.panpf.assemblyadapter.sample.old.ui.list.GameItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.LoadMoreItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.TextItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.UserItem
import com.github.panpf.assemblyadapter.sample.old.vm.ListViewModel
import me.panpf.adapter.paged.AssemblyPagedListAdapter
import me.panpf.adapter.paged.DiffableDiffCallback

class RecyclerPagedSampleFragment : BaseBindingFragment<FmRecyclerBinding>() {

    private val viewModel by viewModels<ListViewModel>()

    private val adapter = AssemblyPagedListAdapter(DiffableDiffCallback()).apply {
        addHeaderItem(TextItem.Factory(), "我是小额头呀！")
        addItemFactory(UserItem.Factory())
        addItemFactory(GameItem.Factory())
        addFooterItem(TextItem.Factory(), "我是小尾巴呀！")
        setMoreItem(LoadMoreItem.Factory())
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FmRecyclerBinding {
        return FmRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmRecyclerBinding, savedInstanceState: Bundle?) {
        binding.recyclerFmRecycler.layoutManager = LinearLayoutManager(activity)
        binding.recyclerFmRecycler.adapter = adapter

        viewModel.list.observe(viewLifecycleOwner, { adapter.submitList(it) })
        viewModel.listStatus.observe(viewLifecycleOwner, {
            if (it is End) adapter.loadMoreFinished(true)
        })
    }
}