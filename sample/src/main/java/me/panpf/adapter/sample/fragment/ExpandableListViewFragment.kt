package me.panpf.adapter.sample.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import me.panpf.adapter.AssemblyAdapter
import me.panpf.adapter.AssemblyExpandableAdapter
import me.panpf.adapter.ItemHolder
import me.panpf.adapter.more.OnLoadMoreListener
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Game
import me.panpf.adapter.sample.bean.GameGroup
import me.panpf.adapter.sample.bean.User
import me.panpf.adapter.sample.bean.UserGroup
import me.panpf.adapter.sample.bindView
import me.panpf.adapter.sample.itemfactory.*
import java.lang.ref.WeakReference
import java.util.*

class ExpandableListViewFragment : Fragment(), OnLoadMoreListener {
    var nextStart = 0
    val groupSize = 20
    val childSize = 5

    private val listView: ExpandableListView by bindView(id = R.id.expandableList_expandableListViewFragment_content)

    var adapter: AssemblyExpandableAdapter? = null
    var headerItemHolder: ItemHolder<String>? = null
    var footerItemHolder: ItemHolder<String>? = null
    var headerItemHolder2: ItemHolder<String>? = null
    var footerItemHolder2: ItemHolder<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expandable_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (adapter != null) {
            listView.setAdapter(adapter)
        } else {
            loadData()
        }
    }

    private fun loadData() {
        LoadTask(WeakReference(this)).execute("")
    }

    override fun onLoadMore(adapter: AssemblyAdapter) {
        loadData()
    }

    class LoadTask constructor(private val refre: WeakReference<ExpandableListViewFragment>) : AsyncTask<String, String, List<Any>>() {
        override fun doInBackground(vararg params: String): List<Any> {
            return refre.get()?.run {
                val dataList = ArrayList<Any>(groupSize)
                for (w in 0 until groupSize) {
                    val groupPosition = w + nextStart
                    if (groupPosition % 2 == 0) {
                        dataList.add(createUserGroup(groupPosition, this))
                    } else {
                        dataList.add(createGameGroup(groupPosition, this))
                    }
                }

                if (nextStart != 0) {
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
                dataList
            } ?: arrayListOf()
        }

        private fun createUserGroup(groupPosition: Int, fragment: ExpandableListViewFragment): UserGroup {
            return fragment.run {
                val userGroup = UserGroup()
                userGroup.userList = ArrayList(childSize)
                for (childPosition in 0 until childSize) {
                    userGroup.userList!!.add(createUser(groupPosition, childPosition))
                }
                userGroup.title = "用户组 " + (groupPosition + 1) + "(" + userGroup.userList!!.size + ")"
                userGroup
            }
        }

        private fun createUser(groupPosition: Int, childPosition: Int): User {
            val user = User()
            user.headResId = R.mipmap.ic_launcher
            user.name = "王大卫 " + (groupPosition + 1) + "." + (childPosition + 1)
            user.sex = if (groupPosition % 2 == 0 && childPosition % 2 == 0) "男" else "女"
            user.age = "" + childPosition
            user.job = "实施工程师"
            user.monthly = "" + 9000 + childPosition + 1
            return user
        }

        private fun createGameGroup(groupPosition: Int, fragment: ExpandableListViewFragment): GameGroup {
            return fragment.run {
                val gameGroup = GameGroup()
                gameGroup.gameList = ArrayList(childSize)
                for (childPosition in 0 until childSize) {
                    gameGroup.gameList!!.add(createGame(groupPosition, childPosition))
                }
                gameGroup.title = "游戏组 " + (groupPosition + 1) + "(" + gameGroup.gameList!!.size + ")"
                gameGroup
            }
        }

        private fun createGame(groupPosition: Int, childPosition: Int): Game {
            val game = Game()
            game.iconResId = R.mipmap.ic_launcher
            game.name = "英雄联盟" + (groupPosition + 1) + "." + (childPosition + 1)
            game.like = if (groupPosition % 2 != 0 && childPosition % 2 != 0) "不喜欢" else "喜欢"
            return game
        }

        override fun onPostExecute(objects: List<Any>) {
            refre.get()?.run {
                val context = context ?: return

                nextStart += groupSize
                if (adapter == null) {
                    adapter = AssemblyExpandableAdapter(objects)

                    headerItemHolder = adapter!!.addHeaderItem(HeaderGroupItem.Factory(), "我是小额头呀！")
                    headerItemHolder2 = adapter!!.addHeaderItem(HeaderGroupItem.Factory(), "唉，我的小额头呢？")
                    adapter!!.addGroupItemFactory(GameGroupItem.Factory())
                    adapter!!.addGroupItemFactory(UserGroupItem.Factory())
                    adapter!!.addChildItemFactory(GameChildItem.Factory())
                    adapter!!.addChildItemFactory(UserChildItem.Factory())
                    adapter!!.setMoreItem(LoadMoreItem.Factory(this))
                    footerItemHolder = adapter!!.addFooterItem(HeaderGroupItem.Factory(), "我是小尾巴呀！")
                    footerItemHolder2 = adapter!!.addFooterItem(HeaderGroupItem.Factory(), "唉，我的小尾巴呢？")

                    listView.setAdapter(adapter)
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
                adapter!!.moreItemHolder?.loadMoreFinished(loadMoreEnd)
            }
        }
    }
}
