package me.xiaopan.assemblyadaptersample.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import me.xiaopan.assemblyadapter.AssemblyAdapter
import me.xiaopan.assemblyadapter.FixedItemInfo
import me.xiaopan.assemblyadapter.OnLoadMoreListener
import me.xiaopan.assemblyadaptersample.R
import me.xiaopan.assemblyadaptersample.bean.Game
import me.xiaopan.assemblyadaptersample.bean.User
import me.xiaopan.assemblyadaptersample.itemfactory.GameItemFactory
import me.xiaopan.assemblyadaptersample.itemfactory.HeaderItemFactory
import me.xiaopan.assemblyadaptersample.itemfactory.LoadMoreItemFactory
import me.xiaopan.assemblyadaptersample.itemfactory.UserItemFactory
import me.xiaopan.ssvt.bindView
import java.util.*

class ListViewFragment : Fragment(), OnLoadMoreListener {
    var nextStart = 0
    val size = 20

    val listView: ListView by bindView(R.id.list_listViewFragment_content)

    var adapter: AssemblyAdapter? = null
    var headerItemInfo: FixedItemInfo? = null
    var headerItemInfo2: FixedItemInfo? = null
    var footerItemInfo: FixedItemInfo? = null
    var footerItemInfo2: FixedItemInfo? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_list_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (adapter != null) {
            listView.adapter = adapter
        } else {
            loadData()
        }
    }

    private fun loadData() {
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
                if (adapter == null) {
                    adapter = AssemblyAdapter(objects)

                    headerItemInfo = adapter!!.addHeaderItem(HeaderItemFactory(), "我是小额头呀！")
                    headerItemInfo2 = adapter!!.addHeaderItem(HeaderItemFactory(), "唉，我的小额头呢！")
                    adapter!!.addItemFactory(UserItemFactory(activity.baseContext))
                    adapter!!.addItemFactory(GameItemFactory(activity.baseContext))
                    footerItemInfo = adapter!!.addFooterItem(HeaderItemFactory(), "我是小尾巴呀！")
                    footerItemInfo2 = adapter!!.addFooterItem(HeaderItemFactory(), "唉，我的小尾巴呢！")
                    adapter!!.setLoadMoreItem(LoadMoreItemFactory(this@ListViewFragment))

                    listView.adapter = adapter
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
                adapter!!.loadMoreFinished(loadMoreEnd)
            }
        }.execute("")
    }

    override fun onLoadMore(adapter: AssemblyAdapter) {
        loadData()
    }
}
