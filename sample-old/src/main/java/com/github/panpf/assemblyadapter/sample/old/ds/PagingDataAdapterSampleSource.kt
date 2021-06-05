package com.github.panpf.assemblyadapter.sample.old.ds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.panpf.assemblyadapter.sample.old.R
import com.github.panpf.assemblyadapter.sample.old.bean.Game
import com.github.panpf.assemblyadapter.sample.old.bean.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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