package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import me.panpf.adapter.paged.AssemblyPagedListAdapter
import me.panpf.adapter.paged.DiffableDiffCallback
import me.panpf.adapter.sample.databinding.FmRecyclerBinding
import me.panpf.adapter.sample.item.GameItem
import me.panpf.adapter.sample.item.LoadMoreItem
import me.panpf.adapter.sample.item.TextItem
import me.panpf.adapter.sample.item.UserItem
import me.panpf.adapter.sample.vm.End
import me.panpf.adapter.sample.vm.ListViewModel
import me.panpf.arch.ktx.bindViewModel

class RecyclerPagedSampleFragment : BaseBindingFragment<FmRecyclerBinding>() {

    private val viewModel by bindViewModel(ListViewModel::class)

    private val adapter = AssemblyPagedListAdapter<Any>(DiffableDiffCallback()).apply {
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.subtitle = "Recycler - PagedList"
    }
}