package me.panpf.adapter.sample.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import me.panpf.adapter.AssemblyAdapter
import me.panpf.adapter.AssemblyExpandableAdapter
import me.panpf.adapter.more.OnLoadMoreListener
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Game
import me.panpf.adapter.sample.bean.GameGroup
import me.panpf.adapter.sample.bean.User
import me.panpf.adapter.sample.bean.UserGroup
import me.panpf.adapter.sample.databinding.FmExpandableBinding
import me.panpf.adapter.sample.item.*
import java.lang.ref.WeakReference
import java.util.*

class ExpandableListViewFragment : BaseBindingFragment<FmExpandableBinding>(), OnLoadMoreListener {
    var nextStart = 0
    val groupSize = 20
    val childSize = 5

    val adapter: AssemblyExpandableAdapter = AssemblyExpandableAdapter().apply {
        addHeaderItem(TextItem.Factory(), "我是小额头呀！")
        addHeaderItem(TextItem.Factory(), "唉，我的小额头呢？")
        addGroupItemFactory(GameGroupItem.Factory())
        addGroupItemFactory(UserGroupItem.Factory())
        addChildItemFactory(GameChildItem.Factory())
        addChildItemFactory(UserChildItem.Factory())
        setMoreItem(LoadMoreItem.Factory(this@ExpandableListViewFragment))
        addFooterItem(TextItem.Factory(), "我是小尾巴呀！")
        addFooterItem(TextItem.Factory(), "唉，我的小尾巴呢？")
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FmExpandableBinding {
        return FmExpandableBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmExpandableBinding, savedInstanceState: Bundle?) {
        binding.expandableFmExpandableList.setAdapter(adapter)

        if (adapter.dataCount <= 0) {
            loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.subtitle = "ExpandableListView"
    }

    private fun loadData() {
        LoadTask(WeakReference(this)).execute("")
    }

    override fun onLoadMore(adapter: AssemblyAdapter) {
        loadData()
    }

    class LoadTask constructor(private val refre: WeakReference<ExpandableListViewFragment>) :
        AsyncTask<String, String, List<Any>>() {
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

        private fun createUserGroup(
            groupPosition: Int,
            fragment: ExpandableListViewFragment
        ): UserGroup {
            return fragment.run {
                val userGroup = UserGroup()
                userGroup.userList = ArrayList(childSize)
                for (childPosition in 0 until childSize) {
                    userGroup.userList!!.add(createUser(groupPosition, childPosition))
                }
                userGroup.title =
                    "用户组 " + (groupPosition + 1) + "(" + userGroup.userList!!.size + ")"
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

        private fun createGameGroup(
            groupPosition: Int,
            fragment: ExpandableListViewFragment
        ): GameGroup {
            return fragment.run {
                val gameGroup = GameGroup()
                gameGroup.gameList = ArrayList(childSize)
                for (childPosition in 0 until childSize) {
                    gameGroup.gameList!!.add(createGame(groupPosition, childPosition))
                }
                gameGroup.title =
                    "游戏组 " + (groupPosition + 1) + "(" + gameGroup.gameList!!.size + ")"
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
                context ?: return

                nextStart += groupSize
                adapter.addAll(objects)
                adapter.setHeaderItemEnabled(1, !adapter.isHeaderItemEnabled(1))
                adapter.setFooterItemEnabled(1, !adapter.isFooterItemEnabled(1))

                val loadMoreEnd = nextStart >= 100
                if (loadMoreEnd) {
                    adapter.setHeaderItemEnabled(0, false)
                    adapter.setFooterItemEnabled(0, false)
                }
                adapter.loadMoreFinished(loadMoreEnd)
            }
        }
    }
}
