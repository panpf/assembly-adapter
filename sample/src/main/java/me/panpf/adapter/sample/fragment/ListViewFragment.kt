package me.panpf.adapter.sample.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import me.panpf.adapter.list.AssemblyListAdapter
import me.panpf.adapter.list.FixedListItemInfo
import me.panpf.adapter.list.OnListLoadMoreListener
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Game
import me.panpf.adapter.sample.bean.User
import me.panpf.adapter.sample.bindView
import me.panpf.adapter.sample.itemfactory.GameItemFactory
import me.panpf.adapter.sample.itemfactory.HeaderItemFactory
import me.panpf.adapter.sample.itemfactory.LoadMoreItemFactory
import me.panpf.adapter.sample.itemfactory.UserItemFactory
import java.util.*

class ListViewFragment : Fragment(), OnListLoadMoreListener {
    var nextStart = 0
    val size = 20

    val listView: ListView by bindView(R.id.list_listViewFragment_content)

    var listAdapter: AssemblyListAdapter? = null
    var headerListItemInfo: FixedListItemInfo? = null
    var headerListItemInfo2: FixedListItemInfo? = null
    var footerListItemInfo: FixedListItemInfo? = null
    var footerListItemInfo2: FixedListItemInfo? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (listAdapter != null) {
            listView.adapter = listAdapter
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
                        val user = User()
                        user.headResId = R.mipmap.ic_launcher
                        user.name = "王大卫" + (index + nextStart + 1)
                        user.sex = if (userStatus) "男" else "女"
                        user.age = "" + (index + nextStart + 1)
                        user.job = "实施工程师"
                        user.monthly = "" + 9000 + index + nextStart + 1
                        dataList.add(user)
                        userStatus = !userStatus
                    } else {
                        val game = Game()
                        game.iconResId = R.mipmap.ic_launcher
                        game.name = "英雄联盟" + (index + nextStart + 1)
                        game.like = if (gameStatus) "不喜欢" else "喜欢"
                        dataList.add(game)
                        gameStatus = !gameStatus
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
                if (listAdapter == null) {
                    listAdapter = AssemblyListAdapter(objects)

                    headerListItemInfo = listAdapter!!.addHeaderItem(HeaderItemFactory(), "我是小额头呀！")
                    headerListItemInfo2 = listAdapter!!.addHeaderItem(HeaderItemFactory(), "唉，我的小额头呢！")
                    listAdapter!!.addItemFactory(UserItemFactory(appContext))
                    listAdapter!!.addItemFactory(GameItemFactory(appContext))
                    footerListItemInfo = listAdapter!!.addFooterItem(HeaderItemFactory(), "我是小尾巴呀！")
                    footerListItemInfo2 = listAdapter!!.addFooterItem(HeaderItemFactory(), "唉，我的小尾巴呢！")
                    listAdapter!!.setLoadMoreItem(LoadMoreItemFactory(this@ListViewFragment))

                    listView.adapter = listAdapter
                } else {
                    listAdapter!!.addAll(objects)

                    headerListItemInfo2!!.isEnabled = !headerListItemInfo2!!.isEnabled
                    footerListItemInfo2!!.isEnabled = !footerListItemInfo2!!.isEnabled
                }

                val loadMoreEnd = nextStart >= 100
                if (loadMoreEnd) {
                    headerListItemInfo!!.isEnabled = false
                    footerListItemInfo!!.isEnabled = false
                }
                listAdapter!!.loadMoreFinished(loadMoreEnd)
            }
        }.execute("")
    }

    override fun onLoadMore(listAdapter: AssemblyListAdapter) {
        loadData()
    }
}
