package me.panpf.adapter.sample.ui

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fm_recycler.*
import me.panpf.adapter.paged.AssemblyPagedListAdapter
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.item.GameItem
import me.panpf.adapter.sample.item.HeaderItem
import me.panpf.adapter.sample.item.LoadMoreItem
import me.panpf.adapter.sample.item.UserItem
import me.panpf.adapter.sample.vm.End
import me.panpf.adapter.sample.vm.ListViewModel
import me.panpf.arch.ktx.bindViewModel

class RecyclerViewFragment : Fragment() {

    private val viewModel by bindViewModel(ListViewModel::class)
    private val adapter = AssemblyPagedListAdapter<Any>().apply {
        addHeaderItem(HeaderItem.Factory(), "我是小额头呀！")
        addItemFactory(UserItem.Factory())
        addItemFactory(GameItem.Factory())
        addFooterItem(HeaderItem.Factory(), "我是小尾巴呀！")
        setMoreItem(LoadMoreItem.Factory())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerFm_recycler.layoutManager = LinearLayoutManager(activity)
        recyclerFm_recycler.adapter = adapter

        viewModel.list.observe(this, androidx.lifecycle.Observer { adapter.submitList(it) })
        viewModel.listStatus.observe(this, Observer {
            when (it) {
                is End -> adapter.loadMoreFinished(true)
            }
        })
    }
}