package me.panpf.adapter.sample.ui

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import me.panpf.adapter.AssemblyAdapter
import me.panpf.adapter.AssemblyRecyclerAdapter
import me.panpf.adapter.ItemHolder
import me.panpf.adapter.more.OnLoadMoreListener
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Game
import me.panpf.adapter.sample.bean.User
import me.panpf.adapter.sample.item.GameItem
import me.panpf.adapter.sample.item.HeaderItem
import me.panpf.adapter.sample.item.LoadMoreItem
import me.panpf.adapter.sample.item.UserItem
import java.lang.ref.WeakReference
import java.util.*

class RecyclerViewFragment : Fragment(), OnLoadMoreListener {
    var nextStart: Int = 0
    var size = 20

    var adapter: AssemblyRecyclerAdapter? = null
    var headerItemHolder: ItemHolder<String>? = null
    var headerItemHolder2: ItemHolder<String>? = null
    var footerItemHolder: ItemHolder<String>? = null
    var footerItemHolder2: ItemHolder<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_recyclerViewFragment_content.layoutManager = LinearLayoutManager(activity)

        if (adapter != null) {
            list_recyclerViewFragment_content.adapter = adapter
        } else {
            loadData()
        }
    }

    private fun loadData() {
        LoadDataTask(WeakReference(this)).execute("")
    }

    override fun onLoadMore(adapter: AssemblyAdapter) {
        loadData()
    }

    class LoadDataTask(private val fragmentRef: WeakReference<RecyclerViewFragment>) : AsyncTask<String, String, List<Any>?>() {

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
                if (adapter == null) {
                    adapter = AssemblyRecyclerAdapter(objects)

                    headerItemHolder = adapter!!.addHeaderItem(HeaderItem.Factory(), "我是小额头呀！")
                    headerItemHolder2 = adapter!!.addHeaderItem(HeaderItem.Factory(), "唉，我的小额头呢？")
                    adapter!!.addItemFactory(UserItem.Factory())
                    adapter!!.addItemFactory(GameItem.Factory())
                    footerItemHolder = adapter!!.addFooterItem(HeaderItem.Factory(), "我是小尾巴呀！")
                    footerItemHolder2 = adapter!!.addFooterItem(HeaderItem.Factory(), "唉，我的小尾巴呢？")
                    adapter!!.setMoreItem(LoadMoreItem.Factory(fragment))

                    list_recyclerViewFragment_content.adapter = adapter
                } else {
                    adapter!!.addAll(objects)
                    headerItemHolder2!!.isEnabled = !headerItemHolder2!!.isEnabled
                    footerItemHolder2!!.isEnabled = !footerItemHolder2!!.isEnabled
                }

                val loadMoreEnd = nextStart >= 100
                if (loadMoreEnd) {
                    headerItemHolder!!.isEnabled = false
                    footerItemHolder!!.isEnabled = false
                }
                adapter!!.moreItemHolder?.isEnabled = !loadMoreEnd
            }
        }
    }
}