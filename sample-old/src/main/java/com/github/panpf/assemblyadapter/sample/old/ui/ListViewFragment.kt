package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.sample.old.R
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.bean.Game
import com.github.panpf.assemblyadapter.sample.old.bean.User
import com.github.panpf.assemblyadapter.sample.old.databinding.FmListBinding
import com.github.panpf.assemblyadapter.sample.old.ui.list.GameItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.LoadMoreItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.TextItem
import com.github.panpf.assemblyadapter.sample.old.ui.list.UserItem
import me.panpf.adapter.AssemblyAdapter
import me.panpf.adapter.AssemblyListAdapter
import me.panpf.adapter.more.OnLoadMoreListener
import java.lang.ref.WeakReference
import java.util.*

class ListViewFragment : BaseBindingFragment<FmListBinding>(), OnLoadMoreListener {
    var nextStart = 0
    val size = 20

    val listAdapter = AssemblyListAdapter().apply {
        addHeaderItem(TextItem.Factory(), "我是小额头呀！")
        addItemFactory(UserItem.Factory())
        addItemFactory(GameItem.Factory())
        addFooterItem(TextItem.Factory(), "我是小尾巴呀！")
        setMoreItem(LoadMoreItem.Factory(this@ListViewFragment))
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmListBinding {
        return FmListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmListBinding, savedInstanceState: Bundle?) {
        binding.listFmList.adapter = listAdapter
        loadData()
    }

    private fun loadData() {
        context?.applicationContext ?: return
        LoadDataTask(WeakReference(this)).execute("")
    }

    override fun onLoadMore(listAdapter: AssemblyAdapter) {
        loadData()
    }

    class LoadDataTask(private val fragmentRef: WeakReference<ListViewFragment>) :
        AsyncTask<String, String, List<Any>>() {

        override fun doInBackground(vararg params: String): List<Any>? {
            val fragment = fragmentRef.get() ?: return null
            fragment.run {
                var position = nextStart
                val dataList = ArrayList<Any>(size)
                var userStatus = true
                var gameStatus = true
                while (position < size + nextStart) {
                    if (position % 2 == 0) {
                        dataList.add(User().apply {
                            headResId = R.mipmap.ic_launcher
                            name = "${position + 1}. 大卫"
                            sex = if (userStatus) "男" else "女"
                            age = (position + 1).toString()
                            job = "实施工程师"
                            monthly = (9000 + position + 1).toString()
                        })
                        userStatus = !userStatus
                    } else {
                        dataList.add(Game().apply {
                            iconResId = R.mipmap.ic_launcher
                            name = "${position + 1}. 英雄联盟"
                            like = if (gameStatus) "不喜欢" else "喜欢"
                        })
                        gameStatus = !gameStatus
                    }
                    position++
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

        override fun onPostExecute(objects: List<Any>) {
            val fragment = fragmentRef.get() ?: return
            fragment.run {
                nextStart += size
                listAdapter.addAll(objects)
                listAdapter.moreItem?.loadMoreFinished(nextStart >= 100)
            }
        }
    }
}
