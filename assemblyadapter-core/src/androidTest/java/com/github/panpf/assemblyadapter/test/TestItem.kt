package com.github.panpf.assemblyadapter.test

import android.content.Context
import android.view.View
import com.github.panpf.assemblyadapter.Item

class TestItem<DATA>(private val context: Context) : Item<DATA> {

    override fun dispatchBindData(position: Int, data: DATA?) {

    }

    override fun getData(): DATA? {
        return null
    }

    override fun getItemView(): View {
        return View(context)
    }

    override fun getPosition(): Int {
        return 0
    }
}