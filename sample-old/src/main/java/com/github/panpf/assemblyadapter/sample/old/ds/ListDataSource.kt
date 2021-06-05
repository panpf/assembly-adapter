package com.github.panpf.assemblyadapter.sample.old.ds

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.github.panpf.assemblyadapter.sample.old.R
import com.github.panpf.assemblyadapter.sample.old.base.Initialize
import com.github.panpf.assemblyadapter.sample.old.base.ListStatus
import com.github.panpf.assemblyadapter.sample.old.bean.Game
import com.github.panpf.assemblyadapter.sample.old.bean.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListDataSource(private val status: MutableLiveData<ListStatus>) :
    PageKeyedDataSource<Int, Any>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Any>
    ) {
        status.postValue(Initialize())
        val dataList = mutableListOf<Any>().apply {
            var userStatus = true
            var gameStatus = true
            for (i in 0 until params.requestedLoadSize) {
                @Suppress("UnnecessaryVariable")
                val position = i
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
        callback.onResult(dataList, null, 1)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Any>) {
        GlobalScope.launch {
            delay(1500)
            val dataList = mutableListOf<Any>().apply {
                if (params.key >= 5) {
                    return@apply
                }
                var userStatus = true
                var gameStatus = true
                for (i in 0 until params.requestedLoadSize) {
                    val position = params.key * params.requestedLoadSize + i
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
            callback.onResult(dataList, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Any>) {
        GlobalScope.launch {
            delay(1500)
            val dataList = mutableListOf<Any>().apply {
                if (params.key < 0) {
                    return@apply
                }
                var userStatus = true
                var gameStatus = true
                for (i in 0 until params.requestedLoadSize) {
                    val position = params.key * params.requestedLoadSize + i
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
            callback.onResult(dataList, params.key - 1)
        }
    }

    class Factory(private val status: MutableLiveData<ListStatus>) :
        DataSource.Factory<Int, Any>() {
        override fun create(): DataSource<Int, Any> = ListDataSource(status)
    }
}