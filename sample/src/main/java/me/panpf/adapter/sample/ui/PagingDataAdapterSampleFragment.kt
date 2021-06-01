package me.panpf.adapter.sample.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.panpf.adapter.FixedItem
import me.panpf.adapter.paged.DiffableDiffCallback
import me.panpf.adapter.paging.AssemblyPagingDataAdapter
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Game
import me.panpf.adapter.sample.bean.User
import me.panpf.adapter.sample.databinding.FmRecyclerBinding
import me.panpf.adapter.sample.item.GameItem
import me.panpf.adapter.sample.item.LoadMoreItem
import me.panpf.adapter.sample.item.TextItem
import me.panpf.adapter.sample.item.UserItem
import me.panpf.arch.ktx.bindViewModel

class PagingDataAdapterSampleFragment : BaseBindingFragment<FmRecyclerBinding>() {

    private val viewModel by bindViewModel(PagingDataAdapterSampleViewModel::class)

    private var headerItem: FixedItem<String>? = null
    private var footerItem: FixedItem<String>? = null

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FmRecyclerBinding {
        return FmRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmRecyclerBinding, savedInstanceState: Bundle?) {
        val pagingDataAdapter = AssemblyPagingDataAdapter<Any>(DiffableDiffCallback()).apply {
            headerItem = addHeaderItem(
                TextItem.Factory()
                    .setOnItemClickListener { context, view, position, positionInPart, data ->
                        headerItem?.setEnabled(false)
                    }, "我是小额头呀！"
            )
            addItemFactory(UserItem.Factory())
            addItemFactory(GameItem.Factory())
            footerItem = addFooterItem(
                TextItem.Factory()
                    .setOnItemClickListener { context, view, position, positionInPart, data ->
                        footerItem?.setEnabled(false)
                    }, "我是小尾巴呀！"
            )
            setMoreItem(LoadMoreItem.Factory())
        }

        binding.recyclerFmRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = pagingDataAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingDataFlow.collect {
                pagingDataAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataAdapter.loadStateFlow.collect {
                when (it.append) {
                    is LoadState.NotLoading -> {
                        pagingDataAdapter.loadMoreFinished(it.append.endOfPaginationReached)
                    }
                    is LoadState.Error -> {
                        pagingDataAdapter.loadMoreFailed()
                    }
                    is LoadState.Loading -> {
                        pagingDataAdapter.loadMoreFinished(false)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.subtitle = "Recycler - PagingDataAdapter"
    }
}

class PagingDataAdapterSampleViewModel(application: Application) : AndroidViewModel(application) {
    val pagingDataFlow =
        Pager(PagingConfig(20, 2, false, 20), 0, { PagingDataAdapterSampleSource() }).flow
}

class PagingDataAdapterSampleSource : PagingSource<Int, Any>() {
    override fun getRefreshKey(state: PagingState<Int, Any>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(2000)
            val start = params.key ?: 0
            if (start == 100) {
                LoadResult.Page(mutableListOf<Any>(), null, null)
            } else {
                val dataList = mutableListOf<Any>().apply {
                    var userStatus = true
                    var gameStatus = true
                    start.until(start + params.loadSize).forEach { position ->
                        if (position % 2 == 0) {
                            add(User().apply {
                                headResId = R.mipmap.ic_launcher
                                name = "${position + 1}. 大卫"
                                sex = if (userStatus) "男" else "女"
                                age = (position + 1).toString()
                                job = "实施工程师"
                                monthly = (9000 + position + 1).toString()
                            })
                            userStatus = !userStatus
                        } else {
                            add(Game().apply {
                                iconResId = R.mipmap.ic_launcher
                                name = "${position + 1}. 英雄联盟"
                                like = if (gameStatus) "不喜欢" else "喜欢"
                            })
                            gameStatus = !gameStatus
                        }
                    }
                }
                LoadResult.Page(dataList, null, start + params.loadSize)
            }
        }
    }
}