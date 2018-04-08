package me.panpf.adapter.sample.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.panpf.adapter.AssemblyAdapter
import me.panpf.adapter.AssemblyRecyclerAdapter
import me.panpf.adapter.FixedItemInfo
import me.panpf.adapter.more.OnLoadMoreListener
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Game
import me.panpf.adapter.sample.bean.User
import me.panpf.adapter.sample.bindView
import me.panpf.adapter.sample.itemfactory.GameItemFactory
import me.panpf.adapter.sample.itemfactory.HeaderItemFactory
import me.panpf.adapter.sample.itemfactory.LoadMoreItemFactory
import me.panpf.adapter.sample.itemfactory.UserItemFactory
import java.util.*

class RecyclerViewFragment : Fragment(), OnLoadMoreListener {
    var nextStart: Int = 0
    var size = 20

    val recyclerView: RecyclerView by bindView(R.id.list_recyclerViewFragment_content)

    var adapter: AssemblyRecyclerAdapter? = null
    var headerItemInfo: FixedItemInfo? = null
    var headerItemInfo2: FixedItemInfo? = null
    var footerItemInfo: FixedItemInfo? = null
    var footerItemInfo2: FixedItemInfo? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(activity)

        if (adapter != null) {
            recyclerView.adapter = adapter
        } else {
            loadData()
        }
    }

    private fun loadData() {
        val appContext = context?.applicationContext ?: return
        object : AsyncTask<String, String, List<Any>>() {

            override fun doInBackground(vararg params: String): List<Any> {
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

            override fun onPostExecute(objects: List<Any>) {
                if (activity == null) {
                    return
                }

                nextStart += size
                if (adapter == null) {
                    adapter = AssemblyRecyclerAdapter(objects)

                    headerItemInfo = adapter!!.addHeaderItem(HeaderItemFactory(), "我是小额头呀！")
                    headerItemInfo2 = adapter!!.addHeaderItem(HeaderItemFactory(), "唉，我的小额头呢？")
                    adapter!!.addItemFactory(UserItemFactory(appContext))
                    adapter!!.addItemFactory(GameItemFactory(appContext))
                    footerItemInfo = adapter!!.addFooterItem(HeaderItemFactory(), "我是小尾巴呀！")
                    footerItemInfo2 = adapter!!.addFooterItem(HeaderItemFactory(), "唉，我的小尾巴呢？")
                    adapter!!.setLoadMoreItem(LoadMoreItemFactory(this@RecyclerViewFragment))

                    recyclerView.adapter = adapter
                } else {
                    adapter!!.addAll(objects)
                    headerItemInfo2!!.isEnabled = !headerItemInfo2!!.isEnabled
                    footerItemInfo2!!.isEnabled = !footerItemInfo2!!.isEnabled
                }

                val loadMoreEnd = nextStart >= 100
                if (loadMoreEnd) {
                    headerItemInfo!!.isEnabled = false
                    footerItemInfo!!.isEnabled = false
                }
                adapter!!.setDisableLoadMore(loadMoreEnd)
            }
        }.execute("")
    }

    override fun onLoadMore(adapter: AssemblyAdapter) {
        loadData()
    }
}
