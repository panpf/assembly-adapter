package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.sample.old.R
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.bean.Game
import com.github.panpf.assemblyadapter.sample.old.bean.User
import com.github.panpf.assemblyadapter.sample.old.databinding.FmRecyclerBinding
import com.github.panpf.assemblyadapter.sample.old.ui.list.GameItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.LoadMoreItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.TextItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.UserItem
import me.panpf.adapter.AssemblyAdapter
import me.panpf.adapter.AssemblyRecyclerAdapter
import me.panpf.adapter.more.OnLoadMoreListener
import java.lang.ref.WeakReference

class RecyclerLinearLayoutSampleFragment : BaseBindingFragment<FmRecyclerBinding>(),
    OnLoadMoreListener {

    var nextStart: Int = 0
    var size = 20

    private val adapter = AssemblyRecyclerAdapter().apply {
        addHeaderItem(TextItem.Factory(), "我是小额头呀！")
        addHeaderItem(TextItem.Factory(), "唉，我的小额头呢？")
        addItemFactory(UserItem.Factory())
        addItemFactory(GameItem.Factory())
        addFooterItem(TextItem.Factory(), "我是小尾巴呀！")
        addFooterItem(TextItem.Factory(), "唉，我的小尾巴呢？")
        setMoreItem(LoadMoreItem.Factory(this@RecyclerLinearLayoutSampleFragment))
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

        binding.recyclerFmRecycler.layoutManager = LinearLayoutManager(activity)

        if (adapter.dataCount <= 0) loadData()
    }

    private fun loadData() {
        LoadDataTask(WeakReference(this)).execute("")
    }

    override fun onLoadMore(adapter: AssemblyAdapter) {
        loadData()
    }

    class LoadDataTask(private val fragmentRef: WeakReference<RecyclerLinearLayoutSampleFragment>) :
        AsyncTask<String, String, List<Any>?>() {

        override fun doInBackground(vararg params: String): List<Any>? {
            val fragment = fragmentRef.get() ?: return null
            fragment.run {
                var index = 0
                val dataList = ArrayList<Any>(size)
                var userStatus = true
                var gameStatus = true
                while (index < size) {
                    if (index % 2 == 0) {
                        val game = Game()
                        game.iconResId = R.mipmap.ic_launcher
                        game.name = "英雄联盟" + (index + nextStart + 1)
                        game.like = if (gameStatus) "不喜欢" else "喜欢"
                        dataList.add(game)
                        gameStatus = !gameStatus
                    } else {
                        val user = User()
                        user.headResId = R.mipmap.ic_launcher
                        user.name = "王大卫" + (index + nextStart + 1)
                        user.sex = if (userStatus) "男" else "女"
                        user.age = "" + (index + nextStart + 1)
                        user.job = "实施工程师"
                        user.monthly = "" + 9000 + index + nextStart + 1
                        dataList.add(user)
                        userStatus = !userStatus
                    }
                    index++
                }
                if (nextStart != 0) {
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                return dataList
            }
        }

        override fun onPostExecute(objects: List<Any>?) {
            val fragment = fragmentRef.get() ?: return
            fragment.context?.applicationContext ?: return
            fragment.apply {
                nextStart += size
                adapter.addAll(objects)
                adapter.setHeaderItemEnabled(1, !adapter.isHeaderItemEnabled(1))
                adapter.setFooterItemEnabled(1, !adapter.isFooterItemEnabled(1))

                val loadMoreEnd = nextStart >= 100
                if (loadMoreEnd) {
                    adapter.setHeaderItemEnabled(0, false)
                    adapter.setFooterItemEnabled(0, false)
                }
                adapter.setMoreItemEnabled(!loadMoreEnd)
            }
        }
    }
}